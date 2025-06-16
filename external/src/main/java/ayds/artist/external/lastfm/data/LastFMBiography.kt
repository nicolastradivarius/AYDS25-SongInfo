package ayds.artist.external.lastfm.data

data class LastFMBiography (
	val artistName: String,
	val biography: String,
	val articleUrl: String,
	val logoUrl: String = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
)