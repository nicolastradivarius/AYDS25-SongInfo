package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong

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
                        "Release Date: ${song.releaseDate()}\n"

            else -> "Song not found"
        }
    }

    // se hace una función de extensión por dos motivos:
    // 1. es mejor la lectura del código en la función getSongDescriptionText, ya que
    // siempre se hace referencia a "song.algo" entonces para Release Date, se sigue el formato.
    // 2. Esta función hace un formateo visual, por lo que no corresponde ponerla dentro de
    // la clase SpotifySong pues ésta es una clase del modelo (literalmente se encuentra en
    // el modelo/entidades), así que se define acá.
    private fun SpotifySong.releaseDate(): String {
        return when (this.releaseDatePrecision) {
            "day" -> formatDay(this.releaseDate)
            "month" -> formatMonth(this.releaseDate)
            "year" -> formatYear(this.releaseDate)
            else -> this.releaseDate
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

    private val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
}