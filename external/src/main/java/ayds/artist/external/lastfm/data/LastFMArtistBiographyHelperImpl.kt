package ayds.artist.external.lastfm.data

import com.google.gson.Gson
import com.google.gson.JsonObject

private const val ARTIST = "artist"
private const val BIO = "bio"
private const val CONTENT = "content"
private const val URL = "url"
private const val NO_RESULTS = "No Results"

interface LastFMArtistBiographyHelper {
	fun parseArtistBiography(serviceBody: String?, artistName: String): LastFMBiography
}

class LastFMArtistBiographyHelperImpl: LastFMArtistBiographyHelper {
	override fun parseArtistBiography(serviceBody: String?, artistName: String): LastFMBiography {
		val gson = Gson()
		val jobj = gson.fromJson(serviceBody, JsonObject::class.java)

		val artistBiographyJSON = jobj.get(ARTIST).getAsJsonObject()
		val artistBioContent = artistBiographyJSON[BIO].getAsJsonObject().get(CONTENT)
		val artistUrl = artistBiographyJSON.get(URL)
		var biographyText = artistBioContent.asString ?: NO_RESULTS

		return LastFMBiography(artistName, biographyText, artistUrl.asString)
	}
}