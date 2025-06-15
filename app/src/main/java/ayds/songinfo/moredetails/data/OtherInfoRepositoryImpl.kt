package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMBiography
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val service: LastFMService
) : OtherInfoRepository {

    override fun getCard(artistName: String): Card {
        // patrón repository
        var card = otherInfoLocalStorage.getCard(artistName)

        when {
            (card != null) -> {
                card = card.markAsLocal()
            }
            else -> {
                card = this.service.getArticle(artistName).toCard()

                if (card.content.isNotBlank()) {
                    otherInfoLocalStorage.insertCard(card)
                }
            }
        }

        return card
    }

}

private fun LastFMBiography.toCard() =
    Card(artistName, biography, articleUrl, CardSource.LAST_FM)


private fun Card.markAsLocal(): Card {
    return copy(isLocallyStored = true)
}