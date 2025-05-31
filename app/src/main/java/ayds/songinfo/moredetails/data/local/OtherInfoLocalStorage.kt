package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.ArtistBiography

interface OtherInfoLocalStorage {
    fun getArticle(artistName: String): ArtistBiography?
    fun insertArticle(artistBiography: ArtistBiography)
}

internal class OtherInfoLocalStorageImpl(
    private val articleDatabase: ArticleDatabase
) : OtherInfoLocalStorage {

    override fun getArticle(artistName: String): ArtistBiography? {
        val articleEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        // retorna el objeto ArtistBiography si articleEntity existe, o null si no existe
        return articleEntity?.let {
            ArtistBiography(artistName, articleEntity.biography, articleEntity.articleUrl)
        }
    }

    override fun insertArticle(artistBiography: ArtistBiography) {
        Thread {
            articleDatabase.ArticleDao().insertArticle(
                ArticleEntity(
                    artistBiography.artistName,
                    artistBiography.biography,
                    artistBiography.articleUrl
                )
            )
        }.start()
    }
}