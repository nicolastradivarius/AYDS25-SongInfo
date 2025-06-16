package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMBiography
import ayds.artist.external.lastfm.data.LastFMService
import ayds.artist.external.newyorktimes.data.NYT_LOGO_URL
import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.data.NYTimesArticle.EmptyArtistDataExternal
import ayds.artist.external.newyorktimes.data.NYTimesService
import ayds.artist.external.wikipedia.data.WikipediaArticle
import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

interface OtherInfoBroker {
    // patrón broker primitivo: fetch a los tres servicios del modulo external
    fun getCards(artistName: String): List<Card>
    fun getLastFMArtist(artistName: String): Card?
    fun getWikipediaArtist(artistName: String): Card?
    fun getNYTimesArtist(artistName: String): Card?
    fun mapToCard(artistName: String, lastFMBiography: LastFMBiography): Card
    fun mapToCard(artistName: String, wikipediaArticle: WikipediaArticle?): Card
    fun mapToCard(artistName: String, nyTimesArticle: NYTimesArticle): Card
}

internal class OtherInfoBrokerImpl(
    private val lastFMService: LastFMService,
    private val wikipediaService: WikipediaTrackService,
    private val nyTimesService: NYTimesService
) : OtherInfoBroker {

    override fun getCards(artistName: String): List<Card> {
        val cards = mutableListOf<Card>()
        getLastFMArtist(artistName)?.let { cards.add(it) }
        getWikipediaArtist(artistName)?.let { cards.add(it) }
        getNYTimesArtist(artistName)?.let { cards.add(it) }
        return cards
    }

    override fun getLastFMArtist(artistName: String): Card? {
        return lastFMService.getArticle(artistName).let {
            mapToCard(artistName, it)
        }
    }

    override fun getWikipediaArtist(artistName: String): Card? {
        return wikipediaService.getInfo(artistName).let {
            mapToCard(artistName, it)
        }
    }

    override fun getNYTimesArtist(artistName: String): Card? {
        return nyTimesService.getArtistInfo(artistName).let {
            mapToCard(artistName, it)
        }
    }

    override fun mapToCard(artistName: String, lastFMBiography: LastFMBiography): Card {
        return Card(
            name = artistName,
            content = lastFMBiography.biography,
            url = lastFMBiography.articleUrl,
            source = CardSource.LAST_FM,
            logoUrl = lastFMBiography.logoUrl,
        )
    }

    override fun mapToCard(artistName: String, wikipediaArticle: WikipediaArticle?): Card {
        if (wikipediaArticle == null || wikipediaArticle.description.isBlank()) {
            return Card(
                name = artistName,
                content = "",
                url = "",
                source = CardSource.WIKIPEDIA,
                logoUrl = ""
            )
        }
        return Card(
            name = artistName ?: "",
            content = wikipediaArticle?.description ?: "",
            url = wikipediaArticle?.wikipediaURL ?: "",
            source = CardSource.WIKIPEDIA,
            logoUrl = wikipediaArticle.wikipediaLogoURL
        )
    }

    override fun mapToCard(artistName: String, nyTimesArticle: NYTimesArticle): Card {
        var card: Card = Card(
            name = artistName,
            content = "",
            url = "",
            source = CardSource.NY_TIMES,
            logoUrl = ""
        )
        if (nyTimesArticle is NYTimesArticle.NYTimesArticleWithData) {
            card = Card(
                name = artistName,
                content = nyTimesArticle.info ?: "",
                url = nyTimesArticle.url ?: "",
                source = CardSource.NY_TIMES,
                logoUrl = NYT_LOGO_URL
            )
        }

        return card
    }
}