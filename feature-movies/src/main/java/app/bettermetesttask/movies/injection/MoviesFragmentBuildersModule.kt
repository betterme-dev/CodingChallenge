package app.bettermetesttask.movies.injection

import app.bettermetesttask.featurecommon.injection.scopes.FragmentScope
import app.bettermetesttask.movies.sections.MoviesFragment
import app.bettermetesttask.movies.sections.compose.MoviesComposeFragment
import app.bettermetesttask.movies.sections.movie_details.MovieDetailsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MoviesFragmentBuildersModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [MoviesScreenModule::class])
    abstract fun createMoviesFragmentInjector(): MoviesFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [MoviesScreenModule::class])
    abstract fun createMovieDetailsFragmentInjector(): MovieDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [MoviesScreenModule::class])
    abstract fun createMoviesComposeFragmentInjector(): MoviesComposeFragment


}