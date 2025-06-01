package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import org.junit.Assert.*
import org.junit.Test
import java.util.Locale

const val locally_prefix = "[*]\n\n\n"

class ArtistBiographyDescriptionHelperTest {

	private val descriptionHelper = ArtistBiographyDescriptionHelper()

	@Test
	fun `getDescription retorna el HTML correcto para un artista almacenado localmente`() {
		// given
		val artistBiography = ArtistBiography(
			artistName = "Artist",
			biography = "This is a biography.",
			articleUrl = "http://example.com",
			isLocallyStored = true
		)
		val expectedDescription = "$locally_prefix<html><div width=400><font face=\"arial\">This is a biography.</font></div></html>"

		// when
		val result = descriptionHelper.getDescription(artistBiography)

		// then
		assertEquals(expectedDescription, result)
	}

	@Test
	fun `getDescription retorna el HTML correcto para un artista no almacenado localmente`() {
		// given
		val artistBiography = ArtistBiography(
			artistName = "Artist",
			biography = "This is a biography.",
			articleUrl = "http://example.com",
			isLocallyStored = false
		)
		val expectedDescription = "<html><div width=400><font face=\"arial\">This is a biography.</font></div></html>"

		// when
		val result = descriptionHelper.getDescription(artistBiography)

		// then
		assertEquals(expectedDescription, result)
	}

	@Test
	fun `get Description resalta en negrita y mayuscula el nombre del artista en la biografia`() {
		// given
		val artistBiography = ArtistBiography(
			artistName = "Prueba",
			biography = "This is a biography of Prueba.",
			articleUrl = "http://example.com",
			isLocallyStored = false
		)
		val expectedHighlight = "<b>${artistBiography.artistName.uppercase(Locale.getDefault())}</b>"
		val expectedDescription = "<html><div width=400><font face=\"arial\">This is a biography of $expectedHighlight.</font></div></html>"

		// when
		val result = descriptionHelper.getDescription(artistBiography)

		// then
		assertEquals(expectedDescription, result)
	}

	@Test
	fun `getDescription debe convertir los saltos de linea en etiquetas br`() {
		val artistBiography = ArtistBiography("artist", "biography\n", "url", false)

		val result = descriptionHelper.getDescription(artistBiography)

		val expected = "<html><div width=400><font face=\"arial\">biography<br></font></div></html>"
		assertEquals(expected, result)
	}

	@Test
	fun `getDescription debe arreglar slashes dobles en un solo salto de linea`() {
		val artistBiography = ArtistBiography("artist", "biography\\n", "url", false)

		val result = descriptionHelper.getDescription(artistBiography)

		val expected = "<html><div width=400><font face=\"arial\">biography<br></font></div></html>"
		assertEquals(expected, result)
	}

	@Test
	fun `getDescription debe reemplazar comillas simples por espacios`() {
		val artistBiography = ArtistBiography("artist", "biography's text", "url", false)

		val result = descriptionHelper.getDescription(artistBiography)

		val expected = "<html><div width=400><font face=\"arial\">biography s text</font></div></html>"
		assertEquals(expected, result)
	}
}