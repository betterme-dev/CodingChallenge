package app.bettermetesttask.datamovies.repository

import app.bettermetesttask.datamovies.repository.stores.IMoviesLocalStore
import app.bettermetesttask.datamovies.repository.stores.IMoviesRemoteStore
import app.bettermetesttask.datamovies.repository.stores.MoviesMapper
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domaincore.utils.fold
import app.bettermetesttask.domaincore.utils.map
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remoteStore: IMoviesRemoteStore,
    private val localStore: IMoviesLocalStore,
    private val mapper: MoviesMapper,
) : MoviesRepository {

    private var lastFetchTime = 0L

    override suspend fun getMovies(): Result<List<Movie>> {
        val cashUpdateResult = updateMoviesCache()
        val localResult = localStore.getMovies().map { movies ->
            movies.map { mapper.mapFromLocal(it) }
        }
        return cashUpdateResult.fold(
            onSuccess = { localResult },
            onError = { cashUpdateError, _ ->
                localResult.fold(
                    onSuccess = { localData -> Result.Error(cashUpdateError, localData) },
                    onError = { localError, _ ->
                        Timber.tag(LOG_TAG).e(localError, "Failed to get movies from local store")
                        Result.Error(cashUpdateError)
                    }
                )
            }
        )
    }

    override suspend fun getMovie(id: Int): Result<Movie> {
        return localStore.getMovie(id).map { mapper.mapFromLocal(it) }
    }

    override fun observeLikedMovieIds(): Flow<List<Int>> {
        return localStore.observeLikedMoviesIds()
    }

    override suspend fun addMovieToFavorites(movieId: Int) {
        localStore.likeMovie(movieId)
    }

    override suspend fun removeMovieFromFavorites(movieId: Int) {
        localStore.dislikeMovie(movieId)
    }

    private suspend fun updateMoviesCache(): Result<Unit> {
        if (System.currentTimeMillis() - lastFetchTime < CACHE_TIMEOUT) {
            return Result.Success(Unit)
        }
        val remoteResult = remoteStore.getMovies().map { movies ->
            movies.map { mapper.mapFromRemote(it) }
        }
        return when (remoteResult) {
            is Result.Success -> {
                localStore.storeMovies(remoteResult.data.map { mapper.mapToLocal(it) }).fold(
                    onSuccess = {
                        lastFetchTime = System.currentTimeMillis()
                        Timber.tag(LOG_TAG).i("Cache updated successfully at $lastFetchTime")
                        Result.Success(Unit)
                    },
                    onError = { error, _ ->
                        Timber.tag(LOG_TAG).e(error, "Failed to store movies in local store")
                        Result.Error(error)
                    }
                )
            }

            is Result.Error -> {
                Timber.tag(LOG_TAG).e(remoteResult.error, "Failed to get movies from remote store")
                Result.Error(remoteResult.error)
            }
        }
    }

    companion object {
        private const val LOG_TAG = "MoviesRepositoryImpl"
        private const val CACHE_TIMEOUT = 15 * 1000 // 15 seconds
    }
}