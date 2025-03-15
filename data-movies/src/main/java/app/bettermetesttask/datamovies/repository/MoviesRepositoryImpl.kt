package app.bettermetesttask.datamovies.repository

import android.util.Log
import app.bettermetesttask.datamovies.repository.stores.MoviesLocalStore
import app.bettermetesttask.datamovies.repository.stores.MoviesMapper
import app.bettermetesttask.datamovies.repository.stores.MoviesRestStore
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*

Можно много чего было сделать или переделать, я постарался реализовать задачи с как можно меньшим импактом кода(не считая реформата кода :)).
Хотя простора для различия реализаций здесь валом.
Конечно я мог затянуть в проект свои наработки по работе с Result, Flow, MVI, States, Locale Data Store/RemoteDataStore однако выбрал такой путь :)

 */

class MoviesRepositoryImpl @Inject constructor(
    private val localStore: MoviesLocalStore,
    private val remoteStore: MoviesRestStore,
    private val mapper: MoviesMapper
) : MoviesRepository {

    override suspend fun getMovies(): Result<List<Movie>> {
        return Result.of {
            remoteStore.getMovies().forEach {
                localStore.insertOrUpdateMovie(mapper.mapToLocal(it))
            }

            localStore.getMovies().map {
                mapper.mapFromLocal(it)
            }
        }
    }

    override suspend fun getMovie(id: Int): Result<Movie> {
        return Result.of { mapper.mapFromLocal(localStore.getMovie(id)) }
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
}