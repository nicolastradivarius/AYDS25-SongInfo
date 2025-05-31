package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.OtherInfoService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OtherInfoRepositoryTest {

	private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk(relaxed = true)
	private val otherInfoService: OtherInfoService = mockk()
	private lateinit var otherInfoRepository: OtherInfoRepository
	private val artistName = "artistName"
	private val expectedBiography = ArtistBiography(
		artistName = artistName,
		biography = "biography",
		lastFMUrl = "lastFMUrl"
	)

	@Before
	fun onBefore() {
		// Configuración inicial antes de cada prueba
		MockKAnnotations.init(this)
		otherInfoRepository = OtherInfoRepositoryImpl(
			otherInfoLocalStorage = otherInfoLocalStorage,
			otherInfoService = otherInfoService
		)
	}

	@Test
	fun `getArtistBiography no encuentra la biografia en el local storage y llama al servicio`() {
		// given: ¿qué condiciones y datos se necesitan para la prueba?
		// cada vez que el repository real llame a otherInfoLocalStorage.getArticle(artistName), debe devolver null
		every { otherInfoLocalStorage.getArticle(artistName) } returns null
		// cada vez que el repository real llame a otherInfoService.getArticle(artistName), debe devolver expectedBiography
		every { otherInfoService.getArticle(artistName) } returns expectedBiography
		// y cada vez que el repository real llame a otherInfoLocalStorage.insertArticle(expectedBiography), no hace nada (es un mock)
		// (con relaxed = true en la declaración de otherInfoLocalStorage, no es necesario especificar esto, ya que el mock relajado no lanza excepciones)
		//every { otherInfoLocalStorage.insertArticle(expectedBiography) } returns Unit

		// when: ¿cuándo tiene que ocurrir lo definido en el given?
		val result = otherInfoRepository.getArtistBiography(artistName)

		// then: ¿qué tiene que pasar cuando se ejecute lo definido en el when?
		assertEquals(expectedBiography, result)
	}

	@Test
	fun `getArtistBiography encuentra la biografia en el local storage`() {
		// given: ¿qué condiciones y datos se necesitan para la prueba?
		// cada vez que el repository real llame a otherInfoLocalStorage.getArticle(artistName), debe devolver expectedBiography
		every { otherInfoLocalStorage.getArticle(artistName) } returns expectedBiography

		// when: ¿cuándo tiene que ocurrir lo definido en el given?
		val result = otherInfoRepository.getArtistBiography(artistName)

		// then: ¿qué tiene que pasar cuando se ejecute lo definido en el when?
		// el resultado debe ser igual a expectedBiography
		assertEquals(expectedBiography, result)
		// debe estar marcado como local
		assertTrue(result.biography.startsWith("[*]\n\n\n"))

	}
}