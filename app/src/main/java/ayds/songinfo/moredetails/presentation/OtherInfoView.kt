package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.content.Intent
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import ayds.songinfo.R
import ayds.songinfo.moredetails.injector.OtherInfoInjector
import com.squareup.picasso.Picasso

private const val LASTFM_IMAGE_URL =
	"https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

class OtherInfoView : Activity() {
	var uiState: ArtistBiographyUiState = ArtistBiographyUiState()

	private lateinit var articleTextView: TextView
	private lateinit var openUrlButton: Button
	private lateinit var lastFMImageView: ImageView

	private lateinit var presenter: OtherInfoPresenter

	override fun onCreate(savedInstanceState: android.os.Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_other_info)

		initProperties()
		initListeners()
		initPresenter()
		initObservers()
		getArtistInfo()
	}

	private fun initProperties() {
		articleTextView = findViewById(R.id.articleTextView)
		openUrlButton = findViewById(R.id.openUrlButton)
		lastFMImageView = findViewById(R.id.lastFMImageView)
	}

	private fun initListeners() {
		openUrlButton.setOnClickListener {
			navigateToURL(uiState.lastFMUrl)
		}
	}

	private fun initPresenter() {
		OtherInfoInjector.init(this)
		presenter = OtherInfoInjector.presenter
	}

	private fun initObservers() {
		presenter.artistBiographyObservable.subscribe { artistBiography ->
			updateUIState(artistBiography)
			updateUI()
		}
	}

	private fun navigateToURL(url: String) {
		val intent = Intent(Intent.ACTION_VIEW)
		intent.data = url.toUri()
		startActivity(intent)
	}

	private fun getArtistInfo() {
		val artistName = getArtistName()
		// cuando le pedimos el artista a presenter, éste notifica a sus observadores (esta vista)
		presenter.getArtistInfo(artistName)
	}

	private fun getArtistName(): String {
		return intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")
	}

	fun updateUI() {
		runOnUiThread {
			updateLastFMImage()
			updateArticleText()
		}
	}

	private fun updateLastFMImage() =
		Picasso.get().load(LASTFM_IMAGE_URL).into(lastFMImageView as ImageView?)

	private fun updateArticleText() {
		articleTextView.text = Html.fromHtml(uiState.infoHtml, Html.FROM_HTML_MODE_COMPACT)
	}

	private fun updateUIState(artistBiography: ArtistBiographyUiState) {
		uiState = uiState.copy(
			artistName = artistBiography.artistName,
			infoHtml = artistBiography.infoHtml,
			lastFMUrl = artistBiography.lastFMUrl
		)
	}

	companion object {
		const val ARTIST_NAME_EXTRA = "artistName"
	}
}