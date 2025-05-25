package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.data.OtherInfoRepositoryImpl
import ayds.songinfo.moredetails.data.external.LastFMAPI
import ayds.songinfo.moredetails.data.external.OtherInfoService
import ayds.songinfo.moredetails.data.external.OtherInfoServiceImpl
import ayds.songinfo.moredetails.data.local.ArticleDatabase
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorageImpl
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelper
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
private const val ARTICLE_DB_NAME = "database-article"

object OtherInfoInjector {

    lateinit var presenter: OtherInfoPresenter
    lateinit var articleDatabase: ArticleDatabase
    lateinit var lastFMAPI: LastFMAPI

    fun init(context: Context) {
        initLastFMAPI()
        initDatabase(context)

        val otherInfoService: OtherInfoService = OtherInfoServiceImpl(lastFMAPI)
        val otherInfoLocalStorage: OtherInfoLocalStorage = OtherInfoLocalStorageImpl(articleDatabase)
        val repository: OtherInfoRepository = OtherInfoRepositoryImpl(otherInfoLocalStorage, otherInfoService)
        val artistBiographyDescriptionHelper = ArtistBiographyDescriptionHelper()

        presenter = OtherInfoPresenterImpl(repository, artistBiographyDescriptionHelper)
    }

    private fun initLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create<LastFMAPI>(LastFMAPI::class.java)
    }

    private fun initDatabase(context: Context) {
        articleDatabase = databaseBuilder(context, ArticleDatabase::class.java, ARTICLE_DB_NAME).build()
    }
}