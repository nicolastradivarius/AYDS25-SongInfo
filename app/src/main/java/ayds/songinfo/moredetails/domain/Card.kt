package ayds.songinfo.moredetails.domain

data class Card(
    val name: String,
    val content: String,
    val url: String,
    val source: CardSource,
    val isLocallyStored: Boolean = false
)

enum class CardSource {
    LAST_FM
}