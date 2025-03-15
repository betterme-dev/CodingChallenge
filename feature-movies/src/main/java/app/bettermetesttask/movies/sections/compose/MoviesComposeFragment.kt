package app.bettermetesttask.movies.sections.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.bettermetesttask.domaincore.utils.DataError
import app.bettermetesttask.domaincore.utils.Error
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.featurecommon.injection.utils.Injectable
import app.bettermetesttask.featurecommon.injection.viewmodel.SimpleViewModelProviderFactory
import app.bettermetesttask.movies.R
import app.bettermetesttask.movies.sections.MoviesState
import app.bettermetesttask.movies.sections.MoviesViewModel
import coil3.compose.AsyncImage
import javax.inject.Inject
import javax.inject.Provider

class MoviesComposeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelProvider: Provider<MoviesViewModel>

    private val viewModel by viewModels<MoviesViewModel> {
        SimpleViewModelProviderFactory(
            viewModelProvider
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                val viewState by viewModel.moviesStateFlow.collectAsState()
                MoviesComposeScreen(viewState, likeMovie = { movie ->
                    viewModel.likeMovie(movie)
                }, onInitialState = {
                    viewModel.loadMovies()
                })
            }
        }
    }
}

@Composable
private fun MoviesComposeScreen(
    moviesState: MoviesState,
    likeMovie: (Movie) -> Unit,
    onInitialState: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (moviesState) {
            MoviesState.Initial -> {
                onInitialState()
            }

            is MoviesState.Loaded -> {
                MoviesList(moviesState.movies, likeMovie)
            }

            MoviesState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MoviesState.Failed -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = errorStringResource(moviesState.error),
                        color = Color.Red,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    moviesState.movies?.let {
                        MoviesList(it, likeMovie)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onLikeClicked: (Movie) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = movie.posterPath,
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = movie.title, fontSize = 18.sp, color = Color.Black)
                Text(text = movie.description, fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = { onLikeClicked(movie) }) {
                Icon(
                    imageVector = if (movie.liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like Button",
                    tint = if (movie.liked) Color.Red else Color.Gray
                )
            }
        }
    }
}

@Composable
fun MoviesList(movies: List<Movie>, onLikeClicked: (Movie) -> Unit) {
    LazyColumn {
        items(movies) { item ->
            MovieItem(item, onLikeClicked = { movie ->
                onLikeClicked(movie)
            })
        }
    }
}

@Composable
@ReadOnlyComposable
fun errorStringResource(error: Error): String {
    return when (error) {
        is DataError.MissingDataError -> stringResource(
            R.string.error_data_missing_with_message,
            error.message ?: "Unknown"
        )

        is DataError.InvalidDataError -> stringResource(
            R.string.error_data_invalid_with_message,
            error.message ?: "Unknown"
        )

        is DataError.UnknownError -> stringResource(
            R.string.error_data_unknown_with_message,
            error.message ?: "Unknown"
        )

        is DataError.NetworkError.NoInternet -> stringResource(R.string.error_network_no_internet)
        is DataError.NetworkError.RequestTimeout -> stringResource(R.string.error_network_request_timeout)
        is DataError.NetworkError.Serialization -> stringResource(R.string.error_network_serialization)
        is DataError.NetworkError.Unknown -> stringResource(R.string.error_network_unknown)

        is DataError.NetworkError.Http.ClientError -> stringResource(
            R.string.error_http_client_with_code,
            error.code
        )

        is DataError.NetworkError.Http.ServerError -> stringResource(
            R.string.error_http_server_with_code,
            error.code
        )

        is DataError.NetworkError.Http.Unknown -> stringResource(
            R.string.error_http_unknown_with_code,
            error.code
        )

        else -> error.toString()
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun PreviewsMovieItem() {
    MovieItem(
        Movie(
            1,
            "Title",
            "Overview",
            null,
            liked = false
        ),
        onLikeClicked = {}
    )
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun PreviewsMovieItemLiked() {
    MovieItem(
        Movie(
            1,
            "Title",
            "Overview",
            null,
            liked = true
        ),
        onLikeClicked = {}
    )
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun PreviewsMoviesComposeScreen() {
    MoviesComposeScreen(MoviesState.Loaded(
        List(20) { index ->
            Movie(
                index,
                "Title $index",
                "Overview $index",
                null,
                liked = index % 2 == 0,
            )
        }
    ), likeMovie = {}, onInitialState = {})
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun PreviewsMoviesComposeScreenErrorWithData() {
    MoviesComposeScreen(MoviesState.Failed(
        DataError.NetworkError.NoInternet,
        List(20) { index ->
            Movie(
                index,
                "Title $index",
                "Overview $index",
                null,
                liked = index % 2 == 0,
            )
        }
    ), likeMovie = {}, onInitialState = {})
}