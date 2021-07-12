package ru.androidschool.appsearch.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.androidschool.appsearch.databinding.FragmentTvBinding
import ru.androidschool.appsearch.network.MovieApiClient
import ru.androidschool.appsearch.ui.feed.MoviePreviewItem
import timber.log.Timber

class TvShowsFragment : Fragment() {

    private lateinit var dashboardViewModel: TvShowsViewModel
    private var _binding: FragmentTvBinding? = null

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val compositeDisposable = CompositeDisposable()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(TvShowsViewModel::class.java)

        _binding = FragmentTvBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            // textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getTvSeries = MovieApiClient.apiClient.getTvSeries()

        compositeDisposable.add(
            getTvSeries.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val movies = it.results.map { MoviePreviewItem(it) {} }.toList()
                    binding.tvRecyclerView.adapter = adapter.apply { addAll(movies) }
                },
                    {
                        Timber.e(it)
                    })
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}