package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.broker.OtherInfoBroker
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class OtherInfoRepositoryTest {

	private val artistName = "artistName"
	private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk(relaxed = true)
	private val otherInfoBroker: OtherInfoBroker = mockk(relaxed = true)
	private val otherInfoRepository: OtherInfoRepository = OtherInfoRepositoryImpl(
		otherInfoLocalStorage,
		otherInfoBroker
	)

	private val expectedLocalArtist = Card(
		name = artistName,
		content = "biography",
		url = "lastFMUrl",
		source = CardSource.LAST_FM,
		logo = "lastFMLogo",
		isLocallyStored = true
	)

	private val expectedLastFMArtist = Card(
		name = artistName,
		content = "biography",
		url = "lastFMUrl",
		source = CardSource.LAST_FM,
		logo = "lastFMLogo",
		isLocallyStored = false
	)

	@Test
	fun `getCards no encuentra la card en el local storage y llama al broker`() {
		// given: ¿qué condiciones y datos se necesitan para la prueba?
		every { otherInfoLocalStorage.getCards(artistName) } returns emptyList()
		every { otherInfoBroker.getCards(artistName) } returns listOf(expectedLastFMArtist)

		// when: ¿cuándo tiene que ocurrir lo definido en el given?
		val cards = otherInfoRepository.getCards(artistName)
		val lastFMArtist = cards.first { it.source == CardSource.LAST_FM }

		// then: ¿qué tiene que pasar cuando se ejecute lo definido en el when?
		assertEquals(lastFMArtist, expectedLastFMArtist)
		assertFalse(lastFMArtist.isLocallyStored)
	}

	@Test
	fun `getCards encuentra la card en el local storage y no llama al broker`() {
		// given
		every { otherInfoLocalStorage.getCards(artistName) } returns listOf(expectedLocalArtist)
		every { otherInfoBroker.getCards(artistName) } returns emptyList()

		// when
		val cards = otherInfoRepository.getCards(artistName)
		val localArtist = cards.first { it.source == CardSource.LAST_FM }

		// then
		assertEquals(localArtist, expectedLocalArtist)
		assertTrue(localArtist.isLocallyStored)
	}

	@Test
	fun `getCards no encuentra la card en el local storage y el broker devuelve una lista vacia`() {
		// given
		every { otherInfoLocalStorage.getCards(artistName) } returns emptyList()
		every { otherInfoBroker.getCards(artistName) } returns emptyList()

		// when
		val cards = otherInfoRepository.getCards(artistName)

		// then
		assertTrue(cards.isEmpty())
	}
}