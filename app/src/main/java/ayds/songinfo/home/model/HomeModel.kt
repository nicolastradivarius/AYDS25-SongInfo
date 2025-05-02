package ayds.songinfo.home.model

import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.repository.SongRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface HomeModel {

    val songObservable: Observable<Song>

    fun searchSong(term: String)

    fun getSongById(id: String): Song
}

internal class HomeModelImpl(private val repository: SongRepository) : HomeModel {

    override val songObservable = Subject<Song>()

    override fun searchSong(term: String) {
        // busca una canción por término y notifica a los observadores con la canción obtenida
        // es lo mismo que songObservable.notify(repository.getSongByTerm(term))
        // con el let {} se ejecuta el bloque con repository.getSongByTerm(term) como "it"
        repository.getSongByTerm(term).let {
            songObservable.notify(it)
        }
    }

    override fun getSongById(id: String): Song = repository.getSongById(id)
}