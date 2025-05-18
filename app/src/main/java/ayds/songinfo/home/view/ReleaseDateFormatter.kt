package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song

interface ReleaseDateFormatter {
    fun getFormattedReleaseDate(song: Song.SpotifySong): String
}

class ReleaseDateFormatterImpl : ReleaseDateFormatter {
    override fun getFormattedReleaseDate(song: Song.SpotifySong): String {
        return when (song.releaseDatePrecision) {
            "day" -> formatDay(song.releaseDate)
            "month" -> formatMonth(song.releaseDate)
            "year" -> formatYear(song.releaseDate)
            else -> song.releaseDate
        }
    }

    private fun formatDay(releaseDate: String): String {
        // releaseDate viene en formato yyyy-MM-dd
        val parts = releaseDate.split("-")
        val day = parts[2]
        val month = parts[1]
        val year = parts[0]
        return "$day/$month/$year"
    }

    private fun formatMonth(releaseDate: String): String {
        // releaseDate: yyyy-MM
        val parts = releaseDate.split("-")
        val month = parts[1]
        val year = parts[0]
        return "${monthNames[month.toInt()-1]} $year"
    }

    private fun formatYear(releaseDate: String): String {
        // releaseDate: yyyy
        val year = releaseDate.toIntOrNull() ?: return releaseDate
        return if (isLeapYear(year)) "$year" else "$year (not a leap year)"
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    private val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
}