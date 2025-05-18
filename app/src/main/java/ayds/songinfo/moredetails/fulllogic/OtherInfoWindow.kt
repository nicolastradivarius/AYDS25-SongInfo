package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Locale
import com.google.gson.Gson
import com.google.gson.JsonObject
import androidx.core.net.toUri

private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
private const val ARTICLE_DB_NAME = "database-article"
private const val DATABASE_MARKUP = "[*]"
private const val LASTFM_IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val NO_RESULTS = "No Results"

data class ArtistBiography(var artistName: String, var biography: String, var articleUrl: String)

// función de extensión de ArtistBiography para marcar el artículo como local.
// se modifica la biografía de la instancia sobre la cual se llamó a la función
private fun ArtistBiography.markAsLocal() {
    this.biography = DATABASE_MARKUP + "\n" + this.biography
}

class OtherInfoWindow : Activity() {
    private lateinit var artistBiography: ArtistBiography
    private lateinit var articleTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView
    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var articleDatabase: ArticleDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initProperties()
        initListeners()
        initDatabase()
        initLastFMAPI()
        getArtistInfoAsync()
    }

    private fun initProperties() {
        articleTextView = findViewById(R.id.articleTextView)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMImageView = findViewById(R.id.lastFMImageView)
    }

    private fun initListeners() {
        openUrlButton.setOnClickListener {
            navigateToURL(artistBiography.articleUrl)
        }
    }

    private fun initDatabase() {
        articleDatabase = databaseBuilder(this, ArticleDatabase::class.java, ARTICLE_DB_NAME).build()
    }

    private fun initLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create<LastFMAPI>(LastFMAPI::class.java)
    }

    fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        artistBiography = getArtistInfoFromRepository()
        updateUI(artistBiography)
    }

    private fun getArtistInfoFromRepository(): ArtistBiography {
        val artistName = getArtistName()

        // patrón repository
        var article = getArticleFromDatabase(artistName)

        when {
            article != null -> article.markAsLocal()
            else -> {
                article = getArticleFromService(artistName)

                // si se encontró el artículo con descripción en el servicio, se guarda en la base de datos
                if (article.biography.isNotBlank()) {
                    insertArticleToDatabase(article)
                }
            }
        }
        return article
    }

    private fun insertArticleToDatabase(artistBiography: ArtistBiography) {
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

    private fun getArticleFromService(artistName: String): ArtistBiography {
        val callResponse = getArticleFromLastFMService(artistName)
        val artistBiography = getArtistBiographyFromExternalData(callResponse.body(), artistName)

        return artistBiography
    }

    private fun getArtistBiographyFromExternalData(serviceBody: String?, artistName: String): ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceBody, JsonObject::class.java)
        val artistBiographyJSON = jobj.get("artist").getAsJsonObject()
        val artistBioContent = artistBiographyJSON.get("bio").getAsJsonObject().get("content")
        val artistUrl = artistBiographyJSON.get("url")

        var biographyText: String = ""

        if (artistBioContent == null) {
            biographyText = NO_RESULTS
        } else {
            biographyText = artistBioContent.asString.replace("\\n", "\n")
            biographyText = textToHtml(biographyText, artistName)
        }

        return ArtistBiography(artistName, biographyText, artistUrl.asString)
    }

    private fun getArticleFromLastFMService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

    private fun updateUI(artistBiography: ArtistBiography) {
        runOnUiThread(Runnable {
            Picasso.get().load(LASTFM_IMAGE_URL)
                .into(lastFMImageView as ImageView?)
            articleTextView.text = Html.fromHtml(artistBiography.biography, Html.FROM_HTML_MODE_COMPACT)
        })
    }

    private fun getArticleFromDatabase(artistName: String): ArtistBiography? {
        val articleEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        // retorna el objeto ArtistBiography si articleEntity existe, o null si no existe
        return articleEntity?.let {
            ArtistBiography(artistName, articleEntity.biography, articleEntity.articleUrl)
        }
    }

    private fun getArtistName(): String {
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        if (artistName.isNullOrBlank()) {
            throw IllegalArgumentException("Missing Artist Name")
        }
        return artistName
    }

    private fun navigateToURL(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = url.toUri()
        startActivity(intent)
    }

    private fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                ("(?i)$term").toRegex(),
                "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")

        return builder.toString()
    }

    companion object {
        const val ARTIST_NAME_EXTRA: String = "artistName"
    }
}