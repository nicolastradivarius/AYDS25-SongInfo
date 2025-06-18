package ayds.songinfo.moredetails.data.broker.proxies

import ayds.songinfo.moredetails.domain.Card

interface CardProxy {
    fun getCard(artistName: String): Card?
}