package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMBiography
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val otherInfoBroker: OtherInfoBroker
) : OtherInfoRepository {

    override fun getCards(artistName: String): List<Card> {
        // patrón repository
//        var card = otherInfoLocalStorage.getCard(artistName)
//
//        when {
//            (card != null) -> {
//                card = card.markAsLocal()
//            }
//            else -> {
//                card = otherInfoBroker.getArticle(artistName).toCard()
//
//                if (card.content.isNotBlank()) {
//                    otherInfoLocalStorage.insertCard(card)
//                }
//            }
//        }

        var cards = otherInfoBroker.getCards(artistName)

        return cards
    }

}

fun Card.markAsLocal(): Card {
    return copy(isLocallyStored = true)
}