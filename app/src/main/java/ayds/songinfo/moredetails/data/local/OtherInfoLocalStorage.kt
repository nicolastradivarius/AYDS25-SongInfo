package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

interface OtherInfoLocalStorage {
    fun getCard(artistName: String): Card?
    fun insertCard(card: Card)
}

internal class OtherInfoLocalStorageImpl(
    private val cardDatabase: CardDatabase
) : OtherInfoLocalStorage {

    override fun getCard(artistName: String): Card? {
        val cardEntity = cardDatabase.CardDao().getCardByArtistName(artistName)
        return cardEntity?.let {
            Card(artistName, cardEntity.content, cardEntity.url, CardSource.entries[cardEntity.source])
        }
    }

    override fun insertCard(card: Card) {
        cardDatabase.CardDao().insertCard(
            CardEntity(
                card.name,
                card.content,
                card.url,
                card.source.ordinal
            )
        )
    }
}