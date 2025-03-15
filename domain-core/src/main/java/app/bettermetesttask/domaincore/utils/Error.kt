package app.bettermetesttask.domaincore.utils

import kotlin.jvm.javaClass

interface Error

open class DataError : Error {
    class MissingDataError(val message: String? = null) : DataError()
    class InvalidDataError(val message: String? = null) : DataError()
    class UnknownError(val message: String? = null) : DataError()

    sealed class NetworkError : DataError() {
        data object NoInternet : NetworkError()
        data object RequestTimeout : NetworkError()
        data object Serialization : NetworkError()
        data object Unknown : NetworkError()

        sealed class Http(val code: Int) : NetworkError() {
            class ClientError(code: Int) : Http(code)
            class ServerError(code: Int) : Http(code)
            class Unknown(code: Int) : Http(code)

            override fun toString(): String = "${javaClass.simpleName}: $code"
        }
    }

    override fun toString(): String = javaClass.simpleName
}