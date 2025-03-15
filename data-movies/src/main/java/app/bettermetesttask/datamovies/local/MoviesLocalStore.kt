package app.bettermetesttask.datamovies.local

import app.bettermetesttask.datamovies.local.database.dao.MoviesDao
import app.bettermetesttask.datamovies.local.database.entities.LikedMovieEntity
import app.bettermetesttask.datamovies.local.database.entities.MovieEntity
import app.bettermetesttask.datamovies.repository.stores.IMoviesLocalStore
import app.bettermetesttask.domaincore.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class MoviesLocalStore @Inject constructor(
    private val moviesDao: MoviesDao
): IMoviesLocalStore {

    override suspend fun getMovies(): Result<List<MovieEntity>> {
        return Result.of { moviesDao.selectMovies() }
    }

    override suspend fun storeMovies(movies: List<MovieEntity>): Result<Unit> {
        return Result.of { moviesDao.insertMovies(movies) }
    }

    override suspend fun getMovie(id: Int): Result<MovieEntity> {
        return Result.of { moviesDao.selectMovieById(id).first() }
    }

    override suspend fun likeMovie(id: Int): Result<Unit> {
        Timber.tag("LocalStore").i("Like movie $id")
        return Result.of { moviesDao.insertLikedEntry(LikedMovieEntity(id)) }
    }

    override suspend fun dislikeMovie(id: Int): Result<Unit> {
        Timber.tag("LocalStore").i("Dislike movie $id")
        return Result.of { moviesDao.removeLikedEntry(id) }
    }

    override fun observeLikedMoviesIds(): Flow<List<Int>> {
        return moviesDao.selectLikedEntries().map { likedMovies -> likedMovies.map { it.movieId } }
    }
}