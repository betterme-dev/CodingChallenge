package app.bettermetesttask.datamovies.remote

import app.bettermetesttask.datamovies.remote.model.MovieRemoteModel
import app.bettermetesttask.datamovies.repository.stores.IMoviesRemoteStore
import app.bettermetesttask.domaincore.utils.Result
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class MoviesRestStore @Inject constructor() : IMoviesRemoteStore {

    private val statusCodes = listOf(200, 201, 202, 304, 400)

    override suspend fun getMovies(): Result<List<MovieRemoteModel>> {
        return Result.of { fetchMovies() }
    }

    private suspend fun fetchMovies(): List<MovieRemoteModel> {
        val statusCode = statusCodes.random()
        if (statusCode >= 400) {
            throw IllegalStateException("Did not manage to retrieve movies")
        }
        delay(Random.nextLong(500, 3_000))
        return MoviesFactory.createMoviesList()
    }
}