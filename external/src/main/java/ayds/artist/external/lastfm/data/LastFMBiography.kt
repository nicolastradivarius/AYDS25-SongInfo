package ayds.artist.external.lastfm.data

data class LastFMBiography (
	val artistName: String,
	val biography: String,
	val articleUrl: String,
	val isLocallyStored: Boolean = false
)