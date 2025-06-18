package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.presentation.helpers.CardDescriptionHelperImpl
import org.junit.Assert.*
import org.junit.Test
import java.util.Locale

const val locally_prefix = "[*]\n\n\n"

class CardDescriptionHelperTest {

    private val descriptionHelper = CardDescriptionHelperImpl()

    @Test
    fun `getFormattedDescription retorna el HTML correcto para un artista almacenado localmente`() {
        val card = Card(
            name = "Artist",
            content = "This is a biography.",
            url = "example.com",
            source = CardSource.LAST_FM,
            logo = "logo.com",
            isLocallyStored = true
        )
        // given
        val expectedDescription = "$locally_prefix<html><div width=400><font face=\"arial\">This is a biography.</font></div></html>"

        // when
        val result = descriptionHelper.getFormattedDescription(card)

        // then
        assertEquals(expectedDescription, result)
    }

    @Test
    fun `getDescription retorna el HTML correcto para un artista no almacenado localmente`() {
        val card = Card(
            name = "Artist",
            content = "This is a biography.",
            url = "example.com",
            source = CardSource.LAST_FM,
            logo = "logo.com",
            isLocallyStored = false
        )
        // given
        val expectedDescription = "<html><div width=400><font face=\"arial\">This is a biography.</font></div></html>"

        // when
        val result = descriptionHelper.getFormattedDescription(card)

        // then
        assertEquals(expectedDescription, result)
    }

    @Test
    fun `get Description resalta en negrita y mayuscula el nombre del artista en la biografia`() {
        val card = Card(
            name = "Artist",
            content = "This is a biography of Artist.",
            url = "example.com",
            source = CardSource.LAST_FM,
            logo = "logo.com",
            isLocallyStored = false
        )
        // given
        val expectedHighlight = "<b>${card.name.uppercase(Locale.getDefault())}</b>"
        val expectedDescription = "<html><div width=400><font face=\"arial\">This is a biography of $expectedHighlight.</font></div></html>"

        // when
        val result = descriptionHelper.getFormattedDescription(card)

        // then
        assertEquals(expectedDescription, result)
    }

    @Test
    fun `getDescription debe convertir los saltos de linea en etiquetas br`() {
        val card = Card(
            name = "artist",
            content = "biography\n",
            url = "url",
            source = CardSource.LAST_FM,
            logo = "logo.com",
            isLocallyStored = false
        )
        val result = descriptionHelper.getFormattedDescription(card)
        val expected = "<html><div width=400><font face=\"arial\">biography<br></font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `getDescription debe arreglar slashes dobles en un solo salto de linea`() {
        val card = Card(
            name = "artist",
            content = "biography\\n",
            url = "url",
            source = CardSource.LAST_FM,
            logo = "logo.com",
            isLocallyStored = false
        )

        val result = descriptionHelper.getFormattedDescription(card)

        val expected = "<html><div width=400><font face=\"arial\">biography<br></font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `getDescription debe reemplazar comillas simples por espacios`() {
        val card = Card(
            name = "artist",
            content = "biography's text",
            url = "url",
            source = CardSource.LAST_FM,
            logo = "logo.com",
            isLocallyStored = false
        )

        val result = descriptionHelper.getFormattedDescription(card)

        val expected = "<html><div width=400><font face=\"arial\">biography s text</font></div></html>"
        assertEquals(expected, result)
    }
}