package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

interface OtherInfoLocalStorage {
    fun getCards(artistName: String): List<Card>
    fun insertCard(card: Card)
}

internal class OtherInfoLocalStorageImpl(
    private val cardDatabase: CardDatabase
) : OtherInfoLocalStorage {

    override fun getCards(artistName: String): List<Card> {
        val cardsEntity = cardDatabase.CardDao().getCardByArtistName(artistName)
        return cardsEntity.map {
            Card(
                artistName,
                it.content,
                it.url,
                CardSource.entries[it.source],
                it.logoUrl
            )
        }
    }

    override fun insertCard(card: Card) {
        cardDatabase.CardDao().insertCard(
            CardEntity(
                card.name,
                card.content,
                card.url,
                card.source.ordinal,
                card.logo
            )
        )
    }
}