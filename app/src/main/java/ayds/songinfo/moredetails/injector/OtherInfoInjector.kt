package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.data.OtherInfoRepositoryImpl
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.data.broker.OtherInfoBroker
import ayds.songinfo.moredetails.data.broker.OtherInfoBrokerImpl
import ayds.songinfo.moredetails.data.broker.proxies.CardProxy
import ayds.songinfo.moredetails.data.broker.proxies.LastFMProxy
import ayds.songinfo.moredetails.data.broker.proxies.NYTimesProxy
import ayds.songinfo.moredetails.data.broker.proxies.WikipediaProxy
import ayds.songinfo.moredetails.data.local.CardDatabase
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorageImpl
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import ayds.songinfo.moredetails.presentation.helpers.CardDescriptionHelper
import ayds.songinfo.moredetails.presentation.helpers.CardDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.presenter.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.presenter.OtherInfoPresenterImpl

private const val ARTICLE_DB_NAME = "database-article"

object OtherInfoInjector {

    private lateinit var presenter: OtherInfoPresenter
    private lateinit var cardDatabase: CardDatabase
    private lateinit var cardDescriptionHelper: CardDescriptionHelper
    private lateinit var repository: OtherInfoRepository
    private lateinit var otherInfoLocalStorage: OtherInfoLocalStorage
    private lateinit var otherInfoBroker: OtherInfoBroker
    private lateinit var listOfProxies: List<CardProxy>
    private lateinit var lastFMProxy: LastFMProxy
    private lateinit var nyTimesProxy: NYTimesProxy
    private lateinit var wikipediaProxy: WikipediaProxy

    fun init(context: Context) {
        LastFMInjector.init()
        initDatabase(context)
        initHelpers()
        initProxies()
        initRepository()
        initPresenter()
    }

    private fun initHelpers() {
        cardDescriptionHelper = CardDescriptionHelperImpl()
    }

    private fun initDatabase(context: Context) {
        cardDatabase = databaseBuilder(context, CardDatabase::class.java, ARTICLE_DB_NAME).build()
    }

    private fun initProxies() {
        lastFMProxy = LastFMProxy(LastFMInjector.lastFMService)
        nyTimesProxy = NYTimesProxy(NYTimesInjector.nyTimesService)
        wikipediaProxy = WikipediaProxy(WikipediaInjector.wikipediaTrackService)
        listOfProxies = listOf(lastFMProxy, nyTimesProxy, wikipediaProxy)
    }

    private fun initRepository() {
        otherInfoLocalStorage = OtherInfoLocalStorageImpl(cardDatabase)
        otherInfoBroker = OtherInfoBrokerImpl(listOfProxies)
        repository = OtherInfoRepositoryImpl(
            otherInfoLocalStorage,
            otherInfoBroker
        )
    }

    private fun initPresenter() {
        presenter = OtherInfoPresenterImpl(repository, cardDescriptionHelper)
    }

    fun getPresenter(): OtherInfoPresenter {
        return presenter
    }
}