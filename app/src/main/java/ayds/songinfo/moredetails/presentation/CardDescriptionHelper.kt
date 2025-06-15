package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import java.util.Locale

interface CardDescriptionHelper {

    /**
     * Returns a formatted HTML description of the given card.
     *
     * @param card The card containing artist information.
     * @return A string containing the HTML formatted description.
     */
    fun getFormattedDescription(card: Card): String
}

class CardDescriptionHelperImpl: CardDescriptionHelper {

    override fun getFormattedDescription(card: Card): String {
        val prefix = if (card.isLocallyStored) "[*]\n\n\n" else ""
        val html = textToHtml(card.content, card.name)
        return prefix + html
    }

    private fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\\n", "\n")
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