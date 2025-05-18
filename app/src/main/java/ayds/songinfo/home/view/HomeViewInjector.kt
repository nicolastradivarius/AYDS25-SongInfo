package ayds.songinfo.home.view

import ayds.songinfo.home.controller.HomeControllerInjector
import ayds.songinfo.home.model.HomeModelInjector

object HomeViewInjector {

    // ahora se le pide a la factory que nos dé una instancia del formateador.
    // en este caso, le pedimos explicitamente las que crea la factory default.
    // si quisieramos otra, tendríamos que implementarla mediante una clase nueva que
    // extienda a ReleaseDateFormatter y cambiar la clase Factory para que permita
    // crear instancias de nuestro nuevo formateador.
    // Nota: lo que se hace aquí es pedir una instancia particular DE LA FACTORY.
    // luego esta FACTORY, mediante create(), nos da una instancia del formateador
    // que maneja, y se la pasamos al helper.
    private val releaseDateFormatterFactory: ReleaseDateFormatterFactory =
        DefaultReleaseDateFormatterFactory()

    val songDescriptionHelper: SongDescriptionHelper =
        SongDescriptionHelperImpl(releaseDateFormatterFactory.create())

    fun init(homeView: HomeView) {
        HomeModelInjector.initHomeModel(homeView)
        HomeControllerInjector.onViewStarted(homeView)
    }
}