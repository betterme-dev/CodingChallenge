package app.bettermetesttask.datamovies.remote.model

data class MovieRemoteModel(
//    @SerializedName("id")
    val id: Int,
//    @SerializedName("title")
    val title: String?,
//    @SerializedName("description")
    val description: String?,
//    @SerializedName("posterPath")
    val posterPath: String?,
)