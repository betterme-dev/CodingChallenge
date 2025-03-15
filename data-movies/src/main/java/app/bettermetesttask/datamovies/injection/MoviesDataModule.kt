package app.bettermetesttask.datamovies.injection

import android.content.Context
import androidx.room.Room
import app.bettermetesttask.datamovies.local.MoviesLocalStore
import app.bettermetesttask.datamovies.local.database.DB_NAME
import app.bettermetesttask.datamovies.local.database.MoviesDatabase
import app.bettermetesttask.datamovies.local.database.dao.MoviesDao
import app.bettermetesttask.datamovies.remote.MoviesRestStore
import app.bettermetesttask.datamovies.repository.MoviesRepositoryImpl
import app.bettermetesttask.datamovies.repository.stores.IMoviesLocalStore
import app.bettermetesttask.datamovies.repository.stores.IMoviesRemoteStore
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class MoviesDataModule {

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(context: Context): MoviesDatabase {
            return Room.databaseBuilder(context.applicationContext, MoviesDatabase::class.java, DB_NAME)
                .build()
        }

        @Provides
        @Singleton
        fun provideMoviesDao(db: MoviesDatabase): MoviesDao {
            return db.getMoviesDao()
        }

        @Provides
        @Singleton
        fun provideMoviesLocalStore(dao: MoviesDao): IMoviesLocalStore {
            return MoviesLocalStore(dao)
        }

        @Provides
        @Singleton
        fun provideMoviesRemoteStore(): IMoviesRemoteStore {
            return MoviesRestStore()
        }
    }

    @Binds
    abstract fun bindMoviesRepository(repositoryImpl: MoviesRepositoryImpl): MoviesRepository
}