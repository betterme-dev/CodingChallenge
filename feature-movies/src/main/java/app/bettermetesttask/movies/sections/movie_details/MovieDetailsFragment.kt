package app.bettermetesttask.movies.sections.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import app.bettermetesttask.featurecommon.injection.utils.Injectable
import app.bettermetesttask.featurecommon.injection.viewmodel.SimpleViewModelProviderFactory
import app.bettermetesttask.featurecommon.utils.images.GlideApp
import app.bettermetesttask.movies.R
import app.bettermetesttask.movies.databinding.MovieDetailsFragmentBinding
import app.bettermetesttask.movies.databinding.MoviesFragmentBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Viнt@rь on 15.03.2025
 */
class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment), Injectable {

    @Inject
    lateinit var viewModelProvider: Provider<MovieDetailsViewModel>

    private val args by navArgs<MovieDetailsFragmentArgs>()

    private lateinit var binding: MovieDetailsFragmentBinding

    private val viewModel by viewModels<MovieDetailsViewModel> { SimpleViewModelProviderFactory(viewModelProvider) }

    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadMovies(args.movieId)
    }

    override fun onResume() {
        super.onResume()
        job = lifecycleScope.launchWhenCreated {
            viewModel.movieState.collect {
                when (it) {
                    is MovieDetailsState.Loaded -> {
                        GlideApp.with(binding.image)
                            .load(it.movie.posterPath)
                            .into(binding.image)

                        binding.title.text = it.movie.title
                        binding.description.text = it.movie.description
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        job?.cancel()
        super.onDestroyView()
    }
}