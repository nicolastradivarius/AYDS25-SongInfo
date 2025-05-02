package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong

private val monthNames = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {

    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release Date: ${formatReleaseDate(song.releaseDate, song.releaseDatePrecision)}\n"

            else -> "Song not found"
        }
    }

    private fun formatReleaseDate(releaseDate: String, precision: String): String {
            return when (precision) {
                "day" -> formatDay(releaseDate)
                "month" -> formatMonth(releaseDate)
                "year" -> formatYear(releaseDate)
                else -> releaseDate
            }
        }

        private fun formatDay(releaseDate: String): String {
            // releaseDate viene en formato yyyy-MM-dd
            val parts = releaseDate.split("-")
            return if (parts.size == 3) "${parts[2]}/${parts[1]}/${parts[0]}" else releaseDate
        }

        private fun formatMonth(releaseDate: String): String {
            // releaseDate: yyyy-MM
            val parts = releaseDate.split("-")
            return if (parts.size == 2) {
                val monthIndex = parts[1].toIntOrNull()?.minus(1) ?: return releaseDate
                val monthName = monthNames.getOrNull(monthIndex) ?: return releaseDate
                "$monthName, ${parts[0]}"
            } else releaseDate
        }

        private fun formatYear(releaseDate: String): String {
            // releaseDate: yyyy
            val year = releaseDate.toIntOrNull() ?: return releaseDate
            val isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
            return if (isLeap) "$year" else "$year (not a leap year)"
        }
}