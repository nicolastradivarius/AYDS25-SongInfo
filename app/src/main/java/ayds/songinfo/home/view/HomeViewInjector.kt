package ayds.songinfo.home.view

import ayds.songinfo.home.controller.HomeControllerInjector
import ayds.songinfo.home.model.HomeModelInjector

object HomeViewInjector {

    val releaseDateFormatter: ReleaseDateFormatter = ReleaseDateFormatterImpl()
    val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl(releaseDateFormatter)

    fun init(homeView: HomeView) {
        HomeModelInjector.initHomeModel(homeView)
        HomeControllerInjector.onViewStarted(homeView)
    }
}