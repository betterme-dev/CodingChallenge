package app.bettermetesttask.movies.sections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.bettermetesttask.featurecommon.injection.utils.Injectable
import app.bettermetesttask.featurecommon.injection.viewmodel.SimpleViewModelProviderFactory
import app.bettermetesttask.featurecommon.utils.views.gone
import app.bettermetesttask.featurecommon.utils.views.visible
import app.bettermetesttask.movies.R
import app.bettermetesttask.movies.databinding.MoviesFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Provider

class MoviesFragment : Fragment(R.layout.movies_fragment), Injectable {

    @Inject
    lateinit var viewModelProvider: Provider<MoviesViewModel>

    private val adapter: MoviesAdapter by lazy { MoviesAdapter() }

    private lateinit var binding: MoviesFragmentBinding

    private val viewModel by viewModels<MoviesViewModel> { SimpleViewModelProviderFactory(viewModelProvider) }

    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MoviesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvList.adapter = adapter

        adapter.onItemClicked = { movie ->
            viewModel.openMovieDetails(movie)
        }

        adapter.onItemLiked = { movie ->
            viewModel.likeMovie(movie)
        }
    }

    override fun onResume() {
        super.onResume()
        job = lifecycleScope.launchWhenCreated {
            viewModel.moviesStateFlow.collect(::renderMoviesState)
        }
    }

    override fun onDestroyView() {
        job?.cancel()
        super.onDestroyView()
    }

    private fun renderMoviesState(state: MoviesState) {
        with(binding) {
            when (state) {
                MoviesState.Loading -> {
                    rvList.gone()
                    progressBar.visible()
                }
                is MoviesState.Loaded -> {
                    progressBar.gone()
                    rvList.visible()
                    adapter.submitList(state.movies)
                }
                is MoviesState.Error -> {
                    MaterialAlertDialogBuilder(requireContext()) // TODO that will be better as effect but...
                        .setTitle("Error")
                        .setMessage(state.message)
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            viewModel.loadMovies()
                        }
                        .show()
                }
                else -> {
                    // no op
                    progressBar.gone()
                    rvList.gone()
                }
            }
        }
    }
}