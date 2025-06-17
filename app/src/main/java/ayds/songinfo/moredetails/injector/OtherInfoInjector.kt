package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.data.OtherInfoRepositoryImpl
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.data.broker.OtherInfoBroker
import ayds.songinfo.moredetails.data.broker.OtherInfoBrokerImpl
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

    lateinit var presenter: OtherInfoPresenter
    lateinit var cardDatabase: CardDatabase
    lateinit var cardDescriptionHelper: CardDescriptionHelper
    lateinit var repository: OtherInfoRepository
    lateinit var otherInfoLocalStorage: OtherInfoLocalStorage
    lateinit var otherInfoBroker: OtherInfoBroker

    fun init(context: Context) {
        LastFMInjector.init()
        initDatabase(context)

        otherInfoLocalStorage = OtherInfoLocalStorageImpl(cardDatabase)
        otherInfoBroker = OtherInfoBrokerImpl(
            lastFMService = LastFMInjector.lastFMService,
            wikipediaService = WikipediaInjector.wikipediaTrackService,
            nyTimesService = NYTimesInjector.nyTimesService
        )
        repository = OtherInfoRepositoryImpl(
            otherInfoLocalStorage,
            otherInfoBroker
        )
        cardDescriptionHelper = CardDescriptionHelperImpl()

        presenter = OtherInfoPresenterImpl(repository, cardDescriptionHelper)
    }

    private fun initDatabase(context: Context) {
        cardDatabase = databaseBuilder(context, CardDatabase::class.java, ARTICLE_DB_NAME).build()
    }
}