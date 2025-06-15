package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OtherInfoRepositoryTest {

	private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk(relaxed = true)
	private val otherInfoService: LastFMService = mockk()
	private lateinit var otherInfoRepository: OtherInfoRepository
	private val artistName = "artistName"

	private val expectedLocalArtist = ArtistBiography(
		artistName = artistName,
		biography = "biography",
		articleUrl = "lastFMUrl",
		isLocallyStored = true
	)

	private val expectedServiceArtist = ArtistBiography(
		artistName = artistName,
		biography = "biography",
		articleUrl = "lastFMUrl",
		isLocallyStored = false
	)

	@Before
	fun onBefore() {
		// Configuración inicial antes de cada prueba
		otherInfoRepository = OtherInfoRepositoryImpl(
			otherInfoLocalStorage = otherInfoLocalStorage,
			lastFMservice = otherInfoService
		)
	}

	@Test
	fun `getArtistBiography no encuentra la biografia en el local storage y llama al servicio`() {
		// given: ¿qué condiciones y datos se necesitan para la prueba?
		// cada vez que el repository real llame a otherInfoLocalStorage.getArticle(artistName), debe devolver null
		every { otherInfoLocalStorage.getArticle(artistName) } returns null
		// cada vez que el repository real llame a otherInfoService.getArticle(artistName), debe devolver expectedBiography
		every { otherInfoService.getArticle(artistName) } returns expectedServiceArtist
		// y cada vez que el repository real llame a otherInfoLocalStorage.insertArticle(expectedBiography), no hace nada (es un mock)
		// (con relaxed = true en la declaración de otherInfoLocalStorage, no es necesario especificar esto, ya que el mock relajado no lanza excepciones)
		//every { otherInfoLocalStorage.insertArticle(expectedBiography) } returns Unit

		// when: ¿cuándo tiene que ocurrir lo definido en el given?
		val result = otherInfoRepository.getArtistBiography(artistName)

		// then: ¿qué tiene que pasar cuando se ejecute lo definido en el when?
		assertEquals(expectedServiceArtist, result)
		assertFalse(result.isLocallyStored)
	}

	@Test
	fun `getArtistBiography encuentra la biografia en el local storage`() {
		// given: ¿qué condiciones y datos se necesitan para la prueba?
		// cada vez que el repository real llame a otherInfoLocalStorage.getArticle(artistName), debe devolver expectedBiography
		every { otherInfoLocalStorage.getArticle(artistName) } returns expectedLocalArtist

		// when: ¿cuándo tiene que ocurrir lo definido en el given?
		val result = otherInfoRepository.getArtistBiography(artistName)

		// then: ¿qué tiene que pasar cuando se ejecute lo definido en el when?
		// el resultado debe ser igual a expectedBiography
		assertEquals(expectedLocalArtist, result)
		// debe estar marcado como local
		assertTrue(result.isLocallyStored)
	}

	/*
	No funciona porque el mockk no permite verificar llamadas a funciones de extensión
	 */
//	@Test
//	fun `cuando se encuentra en el local storage, se llama a markAsLocal`() {
//		// given: ¿qué condiciones y datos se necesitan para la prueba?
//		// cada vez que el repository real llame a otherInfoLocalStorage.getArticle(artistName), debe devolver expectedBiography
//		every { otherInfoLocalStorage.getArticle(artistName) } returns expectedLocalArtist
//
//		// when: ¿cuándo tiene que ocurrir lo definido en el given?
//		otherInfoRepository.getArtistBiography(artistName)
//
//		// then: ¿qué tiene que pasar cuando se ejecute lo definido en el when?
//		// se llamó a la funcion markAsLocal
//		verify(exactly = 1) { expectedLocalArtist.markAsLocal() }
//	}
}