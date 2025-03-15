package app.bettermetesttask.datamovies.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import app.bettermetesttask.datamovies.local.database.dao.MoviesDao
import app.bettermetesttask.datamovies.local.database.entities.LikedMovieEntity
import app.bettermetesttask.datamovies.local.database.entities.MovieEntity

const val DB_NAME = "movies_database.db"

@Database(version = 1, entities = [MovieEntity::class, LikedMovieEntity::class])
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao
}