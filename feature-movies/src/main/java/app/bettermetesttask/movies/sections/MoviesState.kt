package app.bettermetesttask.movies.sections

import app.bettermetesttask.domaincore.utils.Error
import app.bettermetesttask.domainmovies.entries.Movie

sealed class MoviesState {

    object Initial : MoviesState()

    object Loading : MoviesState()

    data class Loaded(val movies: List<Movie>) : MoviesState()

    data class Failed(val error: Error, val movies: List<Movie>? = null) : MoviesState()
}