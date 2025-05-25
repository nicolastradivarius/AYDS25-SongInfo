package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonObject

private const val ARTIST = "artist"
private const val BIO = "bio"
private const val CONTENT = "content"
private const val URL = "url"
private const val NO_RESULTS = "No Results"

interface OtherInfoService {
    fun getArticle(artistName: String): ArtistBiography
}

internal class OtherInfoServiceImpl(
    private val lastFMAPI: LastFMAPI,
) : OtherInfoService {

    override fun getArticle(artistName: String): ArtistBiography {
        val callResponse = getArticleFromLastFMService(artistName)
        val artistBiography = parseArtistBiography(callResponse.body(), artistName)

        return artistBiography
    }

    private fun getArticleFromLastFMService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

    private fun parseArtistBiography(serviceBody: String?, artistName: String): ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceBody, JsonObject::class.java)

        val artistBiographyJSON = jobj.get(ARTIST).getAsJsonObject()
        val artistBioContent = artistBiographyJSON[BIO].getAsJsonObject().get(CONTENT)
        val artistUrl = artistBiographyJSON.get(URL)
        var biographyText = artistBioContent.asString ?: NO_RESULTS

        return ArtistBiography(artistName, biographyText, artistUrl.asString)
    }

}