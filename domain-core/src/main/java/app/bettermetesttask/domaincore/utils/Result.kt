package app.bettermetesttask.domaincore.utils

sealed class Result<out T> {

    companion object {
        inline fun <T> of(block: () -> T): Result<T> {
            return runCatching { block() }
                .fold({
                    Success(it)
                }, {
                    Error(it, null)
                })
        }
    }

    data class Success<out T>(val data: T) : Result<T>()

    data class Error<out T>(val error: Throwable, val data: T? = null) : Result<T>()

    override fun toString(): String =
        when (this) {
            is Success<*> -> "Success[data= $data]"
            is Error<*> -> "Error[throwable= $error]"
        }
}

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(error, data?.let{transform(it)})
    }
}

inline fun <T, R> Result<T>.fold(
    onSuccess: (data: T) -> R,
    onError: (error: Throwable, data: T?) -> R,
): R {
    return when (this) {
        is Result.Success -> onSuccess(data)
        is Result.Error -> onError(error, data)
    }
}