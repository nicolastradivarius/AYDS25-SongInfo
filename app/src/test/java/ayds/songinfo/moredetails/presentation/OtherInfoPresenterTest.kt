package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import ayds.songinfo.moredetails.presentation.helpers.CardDescriptionHelper
import ayds.songinfo.moredetails.presentation.presenter.OtherInfoPresenterImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {
    private val artistName = "artistName"
    private val otherInfoRepository: OtherInfoRepository = mockk(relaxed = true)
    private val cardDescriptionHelper: CardDescriptionHelper = mockk(relaxed = true)
    private val otherInfoPresenter = OtherInfoPresenterImpl(
        otherInfoRepository,
        cardDescriptionHelper
    )

    @Test
    fun `getCards llama al repositorio y notifica a los observers`() {
        // given
        val card = Card(
            name = artistName,
            content = "description",
            url = "example.com",
            source = CardSource.LAST_FM,
            logo = "logo.com"
        )
        var expectedCards = CardsUIState(
            listOf(
                CardUIState(
                    artist = card.name,
                    description = "formatted description",
                    url = card.url,
                    source = card.source.toString(),
                    logo = card.logo
                )
            )
        )
        every { otherInfoRepository.getCards(artistName) } returns listOf(card)
        every { cardDescriptionHelper.getFormattedDescription(card) } returns "formatted description"
        // armo una funcion para suscribirme al observer del presenter
        val onCardsUpdate: (cardsUIState: CardsUIState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.cardsObservable.subscribe(onCardsUpdate)

        // when
        otherInfoPresenter.getCards(artistName)

        // then
        verify { onCardsUpdate(expectedCards) }
    }

    @Test
    fun `getCards obtiene una lista vacia del repositorio y notifica a los observers`() {
        // given
        val expectedCards = CardsUIState(emptyList())
        every { otherInfoRepository.getCards(artistName) } returns emptyList()
        every { cardDescriptionHelper.getFormattedDescription(any()) } returns "formatted description"
        // armo una funcion para suscribirme al observer del presenter
        val onCardsUpdate: (cardsUIState: CardsUIState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.cardsObservable.subscribe(onCardsUpdate)

        // when
        otherInfoPresenter.getCards(artistName)

        // then
        verify { onCardsUpdate(expectedCards) }
    }

    @Test
    fun `getCards llama a la funcion del helper`() {
        // given
        val card = Card(
            name = artistName,
            content = "description",
            url = "example.com",
            source = CardSource.LAST_FM,
            logo = "logo.com"
        )
        every { otherInfoRepository.getCards(artistName) } returns listOf(card)

        // when
        otherInfoPresenter.getCards(artistName)

        // then
        verify (exactly = 1) { cardDescriptionHelper.getFormattedDescription(card) }
    }
}