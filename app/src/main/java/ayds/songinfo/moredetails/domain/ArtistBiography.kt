package ayds.songinfo.moredetails.domain

data class ArtistBiography(
    val artistName: String,
    val biography: String,
    val articleUrl: String,
    val isLocallyStored: Boolean = false
)