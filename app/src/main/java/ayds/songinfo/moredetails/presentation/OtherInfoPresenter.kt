package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

interface OtherInfoPresenter {
    val cardObservable: Observable<CardUIState>

    /**
     * Obtiene la biografía del artista y notifica a los observadores.
     * Se ejecuta en un hilo separado para evitar bloquear el hilo principal.
     *
     * @param artistName Nombre del artista.
     */
    fun getArtistInfo(artistName: String)
}

internal class OtherInfoPresenterImpl(
    private val repository: OtherInfoRepository,
    private val cardDescriptionHelper: CardDescriptionHelper
) : OtherInfoPresenter {

    override val cardObservable = Subject<CardUIState>()

    override fun getArtistInfo(artistName: String) {
        Thread {
            repository.getCard(artistName).let {
                cardObservable.notify(it.toUIState())
            }
        }.start()
    }

    private fun Card.toUIState() = CardUIState(
        name,
        cardDescriptionHelper.getFormattedDescription(this),
        url,
        source.toString()
    )
}