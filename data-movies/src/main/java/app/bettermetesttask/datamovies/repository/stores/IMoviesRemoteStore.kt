package app.bettermetesttask.datamovies.repository.stores

import app.bettermetesttask.datamovies.remote.model.MovieRemoteModel
import app.bettermetesttask.domaincore.utils.Result

interface IMoviesRemoteStore {

    suspend fun getMovies(): Result<List<MovieRemoteModel>>
}