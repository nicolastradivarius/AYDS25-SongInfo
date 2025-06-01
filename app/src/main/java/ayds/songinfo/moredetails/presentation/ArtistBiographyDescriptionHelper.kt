package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import java.util.Locale

internal class ArtistBiographyDescriptionHelper {

    /**
     * Generates a description for the given artist biography, converting the biography text to HTML format
     * and marking the artist's name if it is locally stored.
     *
     * @param artistBiography The artist biography containing the name, biography text, and storage status.
     * @return A string containing the HTML representation of the artist's biography.
     */
    internal fun getDescription(artistBiography: ArtistBiography): String {
        val prefix = if (artistBiography.isLocallyStored) "[*]\n\n\n" else ""
        return prefix + textToHtml(artistBiography.biography, artistBiography.artistName)
    }

    /**
     * Converts the given text to HTML format, highlighting the specified term in bold.
     *
     * @param text The text to be converted to HTML.
     * @param term The term to be highlighted in the text.
     * @return A string containing the HTML representation of the text with the term highlighted.
     */
    private fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                ("(?i)$term").toRegex(),
                "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")

        return builder.toString()
    }
}