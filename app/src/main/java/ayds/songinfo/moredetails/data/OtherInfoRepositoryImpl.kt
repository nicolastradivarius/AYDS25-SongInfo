package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.broker.OtherInfoBroker
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val otherInfoBroker: OtherInfoBroker
) : OtherInfoRepository {

    override fun getCards(artistName: String): List<Card> {
        var cards = otherInfoLocalStorage.getCards(artistName)

        when {
            cards.isNotEmpty() -> {
                cards = cards.map { it.markAsLocal() }
            } else -> {
                cards = otherInfoBroker.getCards(artistName)

                if (cards.isNotEmpty()) {
                    cards.forEach {
                        otherInfoLocalStorage.insertCard(it)
                    }
                }
            }
        }

        return cards
    }

}

fun Card.markAsLocal(): Card {
    return copy(isLocallyStored = true)
}