package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMBiography
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val lastFMservice: LastFMService
) : OtherInfoRepository {

    override fun getArtistBiography(artistName: String): ArtistBiography {
        // patrón repository
        var article = otherInfoLocalStorage.getArticle(artistName)

        when {
            (article != null) -> {
                article = article.markAsLocal()
            }
            else -> {
                article = lastFMservice.getArticle(artistName).toArtistBiography()

                if (article.biography.isNotBlank()) {
                    otherInfoLocalStorage.insertArticle(article)
                }
            }
        }
        return article
    }

}

private fun LastFMBiography.toArtistBiography() =
    ArtistBiography(artistName, biography, articleUrl)


private fun ArtistBiography.markAsLocal(): ArtistBiography {
    return copy(isLocallyStored = true)
}