package ayds.artist.external.lastfm.injector

import ayds.artist.external.lastfm.data.LastFMAPI
import ayds.artist.external.lastfm.data.LastFMArtistBiographyHelper
import ayds.artist.external.lastfm.data.LastFMService
import ayds.artist.external.lastfm.data.LastFMServiceImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object LastFMInjector {
	lateinit var lastFMAPI: LastFMAPI
	lateinit var lastFMHelper: LastFMArtistBiographyHelper
	lateinit var lastFMService: LastFMService

	fun init() {
		val retrofit = Retrofit.Builder()
			.baseUrl(LASTFM_BASE_URL)
			.addConverterFactory(ScalarsConverterFactory.create())
			.build()
		lastFMAPI = retrofit.create<LastFMAPI>(LastFMAPI::class.java)
		lastFMHelper = LastFMArtistBiographyHelper()

		lastFMService = LastFMServiceImpl(lastFMAPI, lastFMHelper)
	}
}