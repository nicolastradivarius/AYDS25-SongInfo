package ayds.songinfo.moredetails.presentation

private const val LASTFM_IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

data class ArtistBiographyUiState(
    var artistName: String = "",
    var infoHtml: String = "",
    var articleUrl: String = "",
    val imageUrl: String = LASTFM_IMAGE_URL
)