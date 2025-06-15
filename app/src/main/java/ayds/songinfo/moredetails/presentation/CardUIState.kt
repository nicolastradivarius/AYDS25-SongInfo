package ayds.songinfo.moredetails.presentation

data class CardUIState(
    val artistName: String = "",
    val infoHtml: String = "",
    val url: String = "",
    var source: String = ""
)