package ru.androidschool.appsearch.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appsearch.app.AppSearchSession
import androidx.appsearch.exceptions.AppSearchException
import androidx.appsearch.localstorage.LocalStorage
import androidx.fragment.app.Fragment
import com.google.common.util.concurrent.ListenableFuture
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.androidschool.appsearch.data.AppSearchRepository
import ru.androidschool.appsearch.data.MockRepository
import ru.androidschool.appsearch.data.MovieDocument
import ru.androidschool.appsearch.data.MovieMapper
import ru.androidschool.appsearch.data.PersonDocument
import ru.androidschool.appsearch.data.PersonMapper
import ru.androidschool.appsearch.databinding.FragmentHomeBinding
import ru.androidschool.appsearch.network.MovieApiClient
import ru.androidschool.appsearch.ui.person.PersonItem
import timber.log.Timber
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class FeedFragment : Fragment() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val TAG = FeedFragment::class.simpleName

    private var _binding: FragmentHomeBinding? = null

    private val compositeDisposable = CompositeDisposable()
    private val binding get() = _binding!!

    /*
    * Gets the number of available cores
    * (not always the same as the maximum number of cores)
    */
    private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()

    // Instantiates the queue of Runnables as a LinkedBlockingQueue
    private val workQueue: BlockingQueue<Runnable> =
        LinkedBlockingQueue<Runnable>()

    // Sets the amount of time an idle thread waits before terminating
    private val KEEP_ALIVE_TIME = 1L

    // Sets the Time Unit to seconds
    private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS

    // Creates a thread pool manager
//    private val threadPoolExecutor: ThreadPoolExecutor = ThreadPoolExecutor(
//        NUMBER_OF_CORES,       // Initial pool size
//        NUMBER_OF_CORES,       // Max pool size
//        KEEP_ALIVE_TIME,
//        KEEP_ALIVE_TIME_UNIT,
//        workQueue
//    )

    private val threadPoolExecutor = Executors.newFixedThreadPool(2)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionFuture: ListenableFuture<AppSearchSession> = LocalStorage.createSearchSession(
            LocalStorage.SearchContext.Builder(activity?.applicationContext!!, "movies_demo_db")
                .build()
        )

        val repository = AppSearchRepository(sessionFuture, threadPoolExecutor)

        repository.setSchema(
            listOf<Class<*>>(
                MovieDocument::class.java,
                PersonDocument::class.java
            )
        )

        val personToSave =
            MockRepository.getPeople().map { PersonMapper.toPersonDocument(it) }
                .toList()

        repository.saveData(personToSave)

        compositeDisposable.add(
            binding.searchToolbar.onTextChangedObservable.debounce(1, TimeUnit.SECONDS)
                .filter { it.length > 4 }
                .flatMap { repository.search(it) }
                .flatMap { repository.iterateSearchResults(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    val allMovies: MutableList<MovieDocument> = mutableListOf()
                    val allPerson: MutableList<PersonDocument> = mutableListOf()
                    result?.forEach { genericDocument ->
                        val schemaType = genericDocument.schemaType
                        try {
                            if (schemaType == "MovieDocument") {
                                // Converts GenericDocument object to MovieDocument object.
                                allMovies.add(genericDocument.toDocumentClass(MovieDocument::class.java))
                            } else if (schemaType == "PersonDocument") {
                                allPerson.add(genericDocument.toDocumentClass(PersonDocument::class.java))
                            }
                        } catch (e: AppSearchException) {
                            Log.d(TAG, "Failed to convert GenericDocument $e")
                        }
                    }
                    adapter.clear()

                    val movies =
                        allMovies.map { MoviePreviewItem(MovieMapper.fromMovieDocument(it)) {} }
                            .toList()

                    if (movies.isNotEmpty()) {
                        val movieItem = MainCardContainer("Movies", movies)
                        binding.eventsRecyclerView.adapter = adapter.apply { add(movieItem) }
                    }

                    val personItems =
                        allPerson.map { PersonItem(PersonMapper.fromPersonDocument(it)) {} }
                            .toList()

                    if (personItems.isNotEmpty()) {
                        val personResultItem = MainCardContainer("Actors", personItems)
                        binding.eventsRecyclerView.adapter = adapter.apply { add(personResultItem) }
                    }
                },
                    {
                        Log.d(TAG, "Error: $it")
                    })
        )


        compositeDisposable.add(
            MovieApiClient.apiClient.getTvSeries()
                .flatMap
                {
                    repository.saveData(MovieMapper.toMovieDocument(it.results))
                    Observable.just(it.results)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        val movies = it.map { MoviePreviewItem(it) {} }.toList()
                        binding.eventsRecyclerView.adapter = adapter.apply { addAll(movies) }
                    },
                    {
                        Timber.e(it)
                    })
        )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}