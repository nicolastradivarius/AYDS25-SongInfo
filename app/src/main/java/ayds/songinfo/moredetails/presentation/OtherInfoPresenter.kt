package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository

interface OtherInfoPresenter {
    val artistBiographyObservable: Observable<ArtistBiographyUiState>

    fun getArtistInfo(artistName: String)
}

internal class OtherInfoPresenterImpl(
    private val repository: OtherInfoRepository,
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper
) : OtherInfoPresenter {

    override val artistBiographyObservable = Subject<ArtistBiographyUiState>()

    /**
     * Obtiene la biografía del artista y notifica a los observadores.
     * Se ejecuta en un hilo separado para evitar bloquear el hilo principal.
     *
     * @param artistName Nombre del artista.
     */
    override fun getArtistInfo(artistName: String) {
        Thread {
            repository.getArtistBiography(artistName).let {
                artistBiographyObservable.notify(it.toUiState())
            }
        }.start()
    }

    /**
     * Convierte un objeto ArtistBiography a ArtistBiographyUiState.
     *
     * @return ArtistBiographyUiState con los datos del artista.
     */
    private fun ArtistBiography.toUiState() = ArtistBiographyUiState(
        artistName,
        artistBiographyDescriptionHelper.getDescription(this),
        articleUrl
    )
}