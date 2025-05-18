package ayds.songinfo.home.view

interface ReleaseDateFormatterFactory {
    fun createInstance(): ReleaseDateFormatter
}

// por defecto, la factory creará instancias de ReleaseDateFormatterImpl
class DefaultReleaseDateFormatterFactory : ReleaseDateFormatterFactory {
    override fun createInstance(): ReleaseDateFormatter = ReleaseDateFormatterImpl()
}