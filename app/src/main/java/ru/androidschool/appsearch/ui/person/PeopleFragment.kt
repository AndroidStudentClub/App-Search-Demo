package ru.androidschool.appsearch.ui.person

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
import ru.androidschool.appsearch.data.MockRepository
import ru.androidschool.appsearch.databinding.FragmentPeopleBinding
import ru.androidschool.appsearch.network.MovieApiClient
import ru.androidschool.appsearch.ui.feed.MoviePreviewItem
import timber.log.Timber

class PeopleFragment : Fragment() {

    private lateinit var peopleViewModel: PeopleViewModel
    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        peopleViewModel =
            ViewModelProvider(this).get(PeopleViewModel::class.java)

        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        peopleViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actors = MockRepository.getPeople().map { PersonItem(it) {} }
        binding.peopleRecyclerView.adapter = adapter.apply { addAll(actors) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}