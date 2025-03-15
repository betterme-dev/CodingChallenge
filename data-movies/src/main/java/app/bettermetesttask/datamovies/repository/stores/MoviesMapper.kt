package app.bettermetesttask.datamovies.repository.stores

import app.bettermetesttask.datamovies.local.database.entities.MovieEntity
import app.bettermetesttask.datamovies.remote.model.MovieRemoteModel
import app.bettermetesttask.domainmovies.entries.Movie
import javax.inject.Inject

class MoviesMapper @Inject constructor() {

    val mapToLocal: (Movie) -> MovieEntity = {
        with(it) {
            MovieEntity(
                id = id,
                title = title,
                description = description,
                posterPath = posterPath
            )
        }
    }

    val mapFromLocal: (MovieEntity) -> Movie = {
        with(it) {
            Movie(
                id = id,
                title = title,
                description = description,
                posterPath = posterPath
            )
        }
    }

    val mapFromRemote: (MovieRemoteModel) -> Movie = {
        with(it) {
            Movie(
                id = id,
                title = title ?: "",
                description = description ?: "",
                posterPath = posterPath
            )
        }
    }
}
