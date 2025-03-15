package app.bettermetesttask.movies.navigation

import javax.inject.Inject

interface MoviesCoordinator {

    fun navigateToMovieDetails(movieId: Int)
}

class MoviesCoordinatorImpl @Inject constructor(
    private val navigator: MoviesNavigator
) : MoviesCoordinator {

    override fun navigateToMovieDetails(movieId: Int) {
        navigator.navigateToMovieDetails(movieId)
    }
}