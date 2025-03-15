package app.bettermetesttask.domainmovies.interactors

import app.bettermetesttask.domaincore.interactor.DefaultIoDispatcherUseCase
import app.bettermetesttask.domaincore.interactor.UseCaseIoDispatcherWithRequest
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveMovieUseCase @Inject constructor(
    private val repository: MoviesRepository
) : UseCaseIoDispatcherWithRequest<Result<Movie>, Int>() {

    override suspend operator fun invoke(): Result<Movie> {
        return repository.getMovie(request)
    }
}