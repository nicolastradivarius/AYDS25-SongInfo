package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.OtherInfoService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val otherInfoService: OtherInfoService
) : OtherInfoRepository {

    /**
     * Obtiene la biografía del artista.
     * Primero intenta obtenerla de la base de datos local.
     * Si no está disponible, la obtiene del servicio externo y la guarda en la base de datos local si es válida.
     *
     * @param artistName Nombre del artista.
     * @return Biografía del artista.
     */
    override fun getArtistBiography(artistName: String): ArtistBiography {
        // patrón repository
        var article = otherInfoLocalStorage.getArticle(artistName)

        when {
            (article != null) -> {
                article = article.markAsLocal()
            }
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

fun ArtistBiography.markAsLocal(): ArtistBiography {
    return copy(isLocallyStored = true)
}