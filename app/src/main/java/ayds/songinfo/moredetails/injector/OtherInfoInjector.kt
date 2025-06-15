package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.data.OtherInfoRepositoryImpl
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.songinfo.moredetails.data.local.ArticleDatabase
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorageImpl
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelper
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl

private const val ARTICLE_DB_NAME = "database-article"

object OtherInfoInjector {

    lateinit var presenter: OtherInfoPresenter
    lateinit var articleDatabase: ArticleDatabase

    fun init(context: Context) {
        LastFMInjector.init()
        initDatabase(context)

        val otherInfoLocalStorage: OtherInfoLocalStorage = OtherInfoLocalStorageImpl(articleDatabase)
        val repository: OtherInfoRepository = OtherInfoRepositoryImpl(
            otherInfoLocalStorage,
            LastFMInjector.lastFMService
        )
        val artistBiographyDescriptionHelper = ArtistBiographyDescriptionHelper()

        presenter = OtherInfoPresenterImpl(repository, artistBiographyDescriptionHelper)
    }

    private fun initDatabase(context: Context) {
        articleDatabase = databaseBuilder(context, ArticleDatabase::class.java, ARTICLE_DB_NAME).build()
    }
}