package ayds.songinfo.moredetails.presentation

/** Represents the UI state for displaying artist biography information.
 *
 * @property artistName The name of the artist.
 * @property infoHtml The HTML formatted biography information of the artist.
 * @property lastFMUrl The URL to the artist's page on Last.fm.
 */
data class ArtistBiographyUiState(
    val artistName: String = "",
    val infoHtml: String = "",
    val lastFMUrl: String = "",
)