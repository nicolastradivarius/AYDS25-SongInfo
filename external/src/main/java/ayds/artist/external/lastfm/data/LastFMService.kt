package ayds.artist.external.lastfm.data

import com.google.gson.Gson
import com.google.gson.JsonObject

private const val ARTIST = "artist"
private const val BIO = "bio"
private const val CONTENT = "content"
private const val URL = "url"
private const val NO_RESULTS = "No Results"

interface LastFMService {
    fun getArticle(artistName: String): LastFMBiography
}

internal class LastFMServiceImpl(
    private val lastFMAPI: LastFMAPI,
) : LastFMService {

    override fun getArticle(artistName: String): LastFMBiography {
        val callResponse = getArticleFromLastFMService(artistName)
        val artistBiography = parseArtistBiography(callResponse.body(), artistName)

        return artistBiography
    }

    private fun getArticleFromLastFMService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

    // TODO: moverlo a un helper
    private fun parseArtistBiography(serviceBody: String?, artistName: String): LastFMBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceBody, JsonObject::class.java)

        val artistBiographyJSON = jobj.get(ARTIST).getAsJsonObject()
        val artistBioContent = artistBiographyJSON[BIO].getAsJsonObject().get(CONTENT)
        val artistUrl = artistBiographyJSON.get(URL)
        var biographyText = artistBioContent.asString ?: NO_RESULTS

        return LastFMBiography(artistName, biographyText, artistUrl.asString)
    }

}