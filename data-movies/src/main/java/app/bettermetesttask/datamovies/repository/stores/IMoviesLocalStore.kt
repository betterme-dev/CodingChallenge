package app.bettermetesttask.datamovies.repository.stores

import app.bettermetesttask.datamovies.local.database.entities.MovieEntity
import app.bettermetesttask.domaincore.utils.Result
import kotlinx.coroutines.flow.Flow

interface IMoviesLocalStore {

    suspend fun getMovies(): Result<List<MovieEntity>>

    suspend fun storeMovies(movies: List<MovieEntity>): Result<Unit>

    suspend fun getMovie(id: Int): Result<MovieEntity>

    suspend fun likeMovie(id: Int): Result<Unit>

    suspend fun dislikeMovie(id: Int): Result<Unit>

    fun observeLikedMoviesIds(): Flow<List<Int>>
}