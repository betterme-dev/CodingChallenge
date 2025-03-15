package app.bettermetesttask.movies.navigation

import androidx.navigation.NavController
import app.bettermetesttask.featurecommon.utils.navigation.executeSafeNavAction
import app.bettermetesttask.movies.sections.MoviesFragmentDirections
import dagger.Lazy
import javax.inject.Inject

interface MoviesNavigator {
    fun navigateToMovieDetails(movieId : Int)
}

class MoviesNavigatorImpl @Inject constructor(
    private val navController: Lazy<NavController>,
) : MoviesNavigator {

    override fun navigateToMovieDetails(movieId: Int) {
        executeSafeNavAction {
            navController.get().navigate(MoviesFragmentDirections.actionToMovieDetailsFragment(movieId))
        }
    }
}