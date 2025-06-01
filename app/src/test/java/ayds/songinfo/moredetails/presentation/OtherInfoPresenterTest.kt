package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {

	private var otherInfoRepository: OtherInfoRepository = mockk(relaxed = true)
	private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = mockk(relaxed = true)
	private val otherInfoPresenter = OtherInfoPresenterImpl(
		repository = otherInfoRepository,
		artistBiographyDescriptionHelper = artistBiographyDescriptionHelper
	)

	@Test
	fun `getArtistInfo llama al repositorio y notifica a los observadores del presenter`() {
		// given: defino que el repositorio devuelve un mock, y una función para suscribirme al observable
		val artistBiography = ArtistBiography("artistName", "sarasa", "articleUrl")
		val artistObserverCallback: (ArtistBiographyUiState) -> Unit = mockk(relaxed = true)
		val artistName = "artistName"
		every { otherInfoRepository.getArtistBiography(artistName) } returns artistBiography

		// si no agrego esto, cuando se llama a getDescription, al no estar especificado qué hacer con
		// artistBiographyDescriptionHelper.getDescription(this) en la función toUiState(), como el
		// helper es un mockk con relaxed=true, retorna el valor por defecto de un String, que es "".
		// esto provocaba que no haya match entre el artista convertido a UIState (que tiene ""),
		// y el expectedResult (que tiene "description").
		every { artistBiographyDescriptionHelper.getDescription(artistBiography) } returns "description"

		otherInfoPresenter.artistBiographyObservable.subscribe { uiState -> artistObserverCallback(uiState) }

		// when
		otherInfoPresenter.getArtistInfo(artistName)

		// then
		val expectedResult = ArtistBiographyUiState(artistName, "description", "articleUrl")
		verify(exactly = 1) { artistObserverCallback(expectedResult) }
	}

	@Test
	fun `getArtistInfo invoca a funcion del helper cuando llega el artista`() {
		// given
		val artistBiography = ArtistBiography("artistName", "description", "articleUrl")
		val artistName = "artistName"
		every { otherInfoRepository.getArtistBiography(artistName) } returns artistBiography

		// when
		otherInfoPresenter.getArtistInfo(artistName)

		// then
		verify(exactly = 1) { artistBiographyDescriptionHelper.getDescription(artistBiography) }
	}
}