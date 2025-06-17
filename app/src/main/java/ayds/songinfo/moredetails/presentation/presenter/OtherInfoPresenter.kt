package ayds.songinfo.moredetails.presentation.presenter

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import ayds.songinfo.moredetails.presentation.CardUIState
import ayds.songinfo.moredetails.presentation.CardsUIState
import ayds.songinfo.moredetails.presentation.helpers.CardDescriptionHelper

interface OtherInfoPresenter {
    val cardsObservable: Observable<CardsUIState>

    fun getArtistInfo(artistName: String)
}

internal class OtherInfoPresenterImpl(
    private val repository: OtherInfoRepository,
    private val cardDescriptionHelper: CardDescriptionHelper
) : OtherInfoPresenter {

    override val cardsObservable = Subject<CardsUIState>()

    override fun getArtistInfo(artistName: String) {
        Thread {
            val cards = repository.getCards(artistName)
            val cardsUIState = CardsUIState(cards.map { it.toUIState() })
            // notifico la lista de cards
            cardsObservable.notify(cardsUIState)
        }.start()
    }

    private fun Card.toUIState() = CardUIState(
        name,
        cardDescriptionHelper.getFormattedDescription(this),
        url,
        source.toString(),
        logo
    )
}