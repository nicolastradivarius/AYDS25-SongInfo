package ayds.songinfo.moredetails.data.broker.proxies

import ayds.artist.external.wikipedia.data.WikipediaArticle
import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class WikipediaProxy(
    private val wikipediaTrackService: WikipediaTrackService
): CardProxy {
    override fun getCard(artistName: String): Card? {
        val article: WikipediaArticle? = wikipediaTrackService.getInfo(artistName)
        val card = article?.toCard(artistName)
        return card
    }

    private fun WikipediaArticle.toCard(artistName: String): Card {
        return Card(
            name = artistName,
            content = description,
            url = wikipediaURL,
            source = CardSource.WIKIPEDIA,
            logo = wikipediaLogoURL
        )
    }
}