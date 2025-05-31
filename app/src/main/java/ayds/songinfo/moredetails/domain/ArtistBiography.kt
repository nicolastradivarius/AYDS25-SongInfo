package ayds.songinfo.moredetails.domain

/**
 * Represents the biography of an artist, including their name, biography text,
 * the URL of the article, and whether it is stored locally.
 *
 * @property artistName The name of the artist.
 * @property biography The biography text of the artist.
 * @property articleUrl The URL of the article containing the biography.
 * @property isLocallyStored Indicates if the biography is stored locally.
 */
data class ArtistBiography(
    val artistName: String,
    val biography: String,
    val articleUrl: String,
    val isLocallyStored: Boolean = false
)