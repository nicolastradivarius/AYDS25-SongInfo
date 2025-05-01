package ayds.songinfo.home.controller

import ayds.observer.Observer
import ayds.songinfo.home.model.HomeModel
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.view.HomeUiEvent
import ayds.songinfo.home.view.HomeView

interface HomeController {

    fun setHomeView(homeView: HomeView)
}

internal class HomeControllerImpl(
    private val homeModel: HomeModel
) : HomeController {

    private lateinit var homeView: HomeView

    override fun setHomeView(homeView: HomeView) {
        this.homeView = homeView
        // el controller se suscribe a los eventos de la vista con el observer parametrizado
        homeView.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<HomeUiEvent> =
        // el observer recibe el evento y ejecuta la acción correspondiente
        // la sintaxis es así porque Observer es una "functional interface" o SAM (single abstract method)
        // básicamente es un interface que tiene un solo métod0 abstracto y por ende se implementa con un lambda
        Observer { value ->
            when (value) {
                HomeUiEvent.Search -> searchSong()
                HomeUiEvent.MoreDetails -> moreDetails()
                is HomeUiEvent.OpenSongUrl -> openSongUrl()
            }
        }


    private fun searchSong() {
        // Warning: Never use Thread in android! Use coroutines
        Thread {
            homeModel.searchSong(homeView.uiState.searchTerm)
        }.start()
    }

    private fun moreDetails() {
        Thread {
            val song = homeModel.getSongById(homeView.uiState.songId)

            (song as? Song.SpotifySong)?.let {
                homeView.navigateToOtherDetails(song.artistName)
            }
        }.start()
    }

    private fun openSongUrl() {
        homeView.openExternalLink(homeView.uiState.songUrl)
    }
}