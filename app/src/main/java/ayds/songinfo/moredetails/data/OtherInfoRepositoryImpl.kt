package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.OtherInfoService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository

private const val DATABASE_MARKUP = "[*]\n\n\n"

class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val otherInfoService: OtherInfoService
) : OtherInfoRepository {

    override fun getArtistBiography(artistName: String): ArtistBiography {
        // patrón repository
        var article = otherInfoLocalStorage.getArticle(artistName)

        when {
            article != null -> article.markAsLocal()
            else -> {
                article = otherInfoService.getArticle(artistName)

                // si se encontró el artículo con descripción en el servicio, se guarda en la base de datos
                if (article.biography.isNotBlank()) {
                    otherInfoLocalStorage.insertArticle(article)
                }
            }
        }
        return article
    }
}

private fun ArtistBiography.markAsLocal() {
    this.biography = "$DATABASE_MARKUP$biography"
}
