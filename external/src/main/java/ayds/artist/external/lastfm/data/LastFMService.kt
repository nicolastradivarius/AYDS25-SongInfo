package ayds.artist.external.lastfm.data

interface LastFMService {
    fun getArticle(artistName: String): LastFMBiography
}

internal class LastFMServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastFMHelper: LastFMArtistBiographyHelper
) : LastFMService {

    override fun getArticle(artistName: String): LastFMBiography {
        val callResponse = getArticleFromLastFMService(artistName)
        val artistBiography = lastFMHelper.parseArtistBiography(callResponse.body(), artistName)

        return artistBiography
    }

    private fun getArticleFromLastFMService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()
}