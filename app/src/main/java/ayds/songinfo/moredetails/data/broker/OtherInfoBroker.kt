package ayds.songinfo.moredetails.data.broker

import ayds.songinfo.moredetails.data.broker.proxies.CardProxy
import ayds.songinfo.moredetails.domain.Card

interface OtherInfoBroker {
    // patron broker con proxies
    fun getCards(artistName: String): List<Card>
}

internal class OtherInfoBrokerImpl(
    private val otherInfoProxies: List<CardProxy>
) : OtherInfoBroker {

    override fun getCards(artistName: String): List<Card> {
        val cards = mutableListOf<Card>()
        for (proxy in otherInfoProxies) {
            val card = proxy.getCard(artistName)
            if (card != null) {
                cards.add(card)
            }
        }
        return cards
    }
}