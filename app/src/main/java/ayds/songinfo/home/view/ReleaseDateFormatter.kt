package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song

interface ReleaseDateFormatter {
    fun getFormattedReleaseDate(song: Song.SpotifySong): String
}

class ReleaseDateFormatterImpl : ReleaseDateFormatter {
    override fun getFormattedReleaseDate(song: Song.SpotifySong): String {
        return when (song.releaseDatePrecision) {
            "day" -> ReleaseDateDayFormatter.getFormattedReleaseDate(song)
            "month" -> ReleaseDateMonthFormatter.getFormattedReleaseDate(song)
            "year" -> ReleaseDateYearFormatter.getFormattedReleaseDate(song)
            else -> ReleaseDateDefaultFormatter.getFormattedReleaseDate(song)
        }
    }
}

internal object ReleaseDateDayFormatter: ReleaseDateFormatter {
    override fun getFormattedReleaseDate(song: Song.SpotifySong): String {
        // releaseDate viene en formato yyyy-MM-dd
        val parts = song.releaseDate.split("-")
        val day = parts[2]
        val month = parts[1]
        val year = parts[0]
        return "$day/$month/$year"
    }
}

internal object ReleaseDateMonthFormatter: ReleaseDateFormatter {
    private val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    override fun getFormattedReleaseDate(song: Song.SpotifySong): String {
        // releaseDate viene en formato yyyy-MM
        val parts = song.releaseDate.split("-")
        val month = parts[1].toInt()
        val year = parts[0]
        return if (validMonth(month))
            "${monthNames[month - 1]} $year" else year
    }

    private fun validMonth(month: Int): Boolean {
        return month in 1..12
    }
}

internal object ReleaseDateYearFormatter: ReleaseDateFormatter {
    override fun getFormattedReleaseDate(song: Song.SpotifySong): String {
        // releaseDate: yyyy
        val year = song.releaseDate.toIntOrNull() ?: return song.releaseDate
        return if (isLeapYear(year)) "$year" else "$year (not a leap year)"
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
}

internal object ReleaseDateDefaultFormatter: ReleaseDateFormatter {
    override fun getFormattedReleaseDate(song: Song.SpotifySong) = song.releaseDate
}