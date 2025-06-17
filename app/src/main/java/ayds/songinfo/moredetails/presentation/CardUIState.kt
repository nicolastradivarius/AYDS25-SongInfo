package ayds.songinfo.moredetails.presentation

data class CardsUIState(
    val cards: List<CardUIState> = emptyList()
)

data class CardUIState(
    var artist: String = "",
    var description: String = "",
    var url: String = "",
    var source: String = "",
    var logo: String = ""
)