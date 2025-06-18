package ayds.songinfo.moredetails.data.broker.proxies

import ayds.artist.external.newyorktimes.data.NYT_LOGO_URL
import ayds.artist.external.newyorktimes.data.NYTimesArticle.NYTimesArticleWithData
import ayds.artist.external.newyorktimes.data.NYTimesService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class NYTimesProxy(
    private val nyTimesService: NYTimesService
): CardProxy {
    override fun getCard(artistName: String): Card? {
        val nyTimesArticle = nyTimesService.getArtistInfo(artistName)
        var card: Card? = null

        if (nyTimesArticle is NYTimesArticleWithData)
            card = nyTimesArticle.toCard(artistName)

        return card
    }

    private fun NYTimesArticleWithData.toCard(artistName: String): Card {
        return Card(
            name = artistName,
            content = info?: "",
            url = url,
            source = CardSource.NY_TIMES,
            logo = NYT_LOGO_URL
        )
    }
}