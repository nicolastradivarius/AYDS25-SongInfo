package ayds.songinfo.moredetails.data.broker.proxies

import ayds.artist.external.lastfm.data.LastFMBiography
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class LastFMProxy(
    private val lastFMService: LastFMService
): CardProxy {

    override fun getCard(artistName: String): Card? {
        val lastFMTrack = lastFMService.getArticle(artistName)
        return lastFMTrack.toCard(artistName)
    }

    private fun LastFMBiography.toCard(artistName: String): Card {
        return Card(
            name = artistName,
            content = biography,
            url = articleUrl,
            source = CardSource.LAST_FM,
            logo = logoUrl
        )
    }
}