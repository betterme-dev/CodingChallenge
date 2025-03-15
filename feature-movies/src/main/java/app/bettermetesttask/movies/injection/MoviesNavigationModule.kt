package app.bettermetesttask.injection.modules

import androidx.navigation.NavController
import androidx.navigation.findNavController
import app.bettermetesttask.movies.navigation.MoviesCoordinator
import app.bettermetesttask.movies.navigation.MoviesCoordinatorImpl
import app.bettermetesttask.movies.navigation.MoviesNavigator
import app.bettermetesttask.movies.navigation.MoviesNavigatorImpl
import dagger.Module
import dagger.Provides

@Module
class MoviesNavigationModule {

    @Provides
    fun bindNavigator(navigatorImpl: MoviesNavigatorImpl): MoviesNavigator {
        return navigatorImpl
    }

    @Provides
    fun bindCoordinator(coordinatorImpl: MoviesCoordinatorImpl): MoviesCoordinator {
        return coordinatorImpl
    }
}