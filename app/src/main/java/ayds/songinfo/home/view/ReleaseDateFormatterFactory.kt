package ayds.songinfo.home.view

interface ReleaseDateFormatterFactory {
    fun create(): ReleaseDateFormatter
}

// por defecto, la factory creará instancias de ReleaseDateFormatterImpl
class DefaultReleaseDateFormatterFactory : ReleaseDateFormatterFactory {
    override fun create(): ReleaseDateFormatter = ReleaseDateFormatterImpl()
}