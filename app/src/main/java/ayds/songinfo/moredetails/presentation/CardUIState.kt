package ayds.songinfo.moredetails.presentation

data class CardsUIState(
    val cards: List<CardUIState> = emptyList()
)

data class CardUIState(
    var artistName: String = "",
    var infoHtml: String = "",
    var url: String = "",
    var source: String = "",
    var logoUrl: String = ""
)