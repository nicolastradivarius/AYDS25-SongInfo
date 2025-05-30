package ayds.songinfo.home.model.repository

import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong
import ayds.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage

interface SongRepository {
    fun getSongByTerm(term: String): Song
    fun getSongById(id: String): Song
}

internal class SongRepositoryImpl(
    private val spotifyLocalStorage: SpotifyLocalStorage,
    private val spotifyTrackService: SpotifyTrackService
) : SongRepository {

    override fun getSongByTerm(term: String): Song {
        var spotifySong = spotifyLocalStorage.getSongByTerm(term)

        when {
            spotifySong != null -> markSongAsLocal(spotifySong)
            else -> {
                try {
                    spotifySong = spotifyTrackService.getSong(term)

                    spotifySong?.let {
                        when {
                            // hay veces en que una misma canción se busca por nombres distintos
                            // isSavedSong se fija si el ID de la canción ya está, en cuyo caso actualiza el término
                            it.isSavedSong() -> spotifyLocalStorage.updateSongTerm(term, it.id)
                            else -> spotifyLocalStorage.insertSong(term, it)
                        }
                    }
                } catch (e: Exception) {
                    spotifySong = null
                }
            }
        }

        return spotifySong ?: EmptySong
    }

    override fun getSongById(id: String): Song {
        return spotifyLocalStorage.getSongById(id) ?: EmptySong
    }

    private fun SpotifySong.isSavedSong() = spotifyLocalStorage.getSongById(id) != null

    private fun markSongAsLocal(song: SpotifySong) {
        song.isLocallyStored = true
    }
}