package app.bettermetesttask.movies.sections.movie_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domaincore.utils.coroutines.AppDispatchers
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.interactors.AddMovieToFavoritesUseCase
import app.bettermetesttask.domainmovies.interactors.ObserveMovieUseCase
import app.bettermetesttask.domainmovies.interactors.ObserveMoviesUseCase
import app.bettermetesttask.domainmovies.interactors.RemoveMovieFromFavoritesUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MovieDetailsState {
    data object Initial : MovieDetailsState
    data class Loaded(val movie: Movie) : MovieDetailsState
}

class MovieDetailsViewModel @Inject constructor(
    private val observeMovieUseCase: ObserveMovieUseCase
) : ViewModel() {

    private val _movieState = MutableStateFlow<MovieDetailsState>(MovieDetailsState.Initial)
    val movieState = _movieState.asStateFlow()

    fun loadMovies(movieId: Int) {
        viewModelScope.launch {
            when (val result = observeMovieUseCase.get(movieId)) {
                is Result.Success -> _movieState.emit(MovieDetailsState.Loaded(result.data))
                else -> Unit// TODO
            }
        }
    }
}