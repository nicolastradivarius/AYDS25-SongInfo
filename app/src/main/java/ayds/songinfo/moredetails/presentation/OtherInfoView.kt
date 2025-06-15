package ayds.songinfo.moredetails.presentation

import android.annotation.SuppressLint
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
	var uiState: CardUIState = CardUIState()

	private lateinit var cardTextView: TextView
	private lateinit var sourceTextView: TextView
	private lateinit var openUrlButton: Button
	private lateinit var logoImageView: ImageView
	private lateinit var presenter: OtherInfoPresenter

	override fun onCreate(savedInstanceState: android.os.Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_other_info)

		initProperties()
		initListeners()
		initPresenter()
		initObservers()
		displayLastFMImage()
		getCardInfo()
	}

	private fun initProperties() {
		cardTextView = findViewById(R.id.cardTextView)
		openUrlButton = findViewById(R.id.openUrlButton)
		logoImageView = findViewById(R.id.lastFMImageView)
		sourceTextView = findViewById(R.id.sourceTextView)
	}

	private fun initListeners() {
		openUrlButton.setOnClickListener {
			navigateToURL(uiState.url)
		}
	}

	private fun initPresenter() {
		OtherInfoInjector.init(this)
		presenter = OtherInfoInjector.presenter
	}

	private fun initObservers() {
		presenter.cardObservable.subscribe { card ->
			updateUIState(card)
			updateUI()
		}
	}

	private fun navigateToURL(url: String) {
		val intent = Intent(Intent.ACTION_VIEW)
		intent.data = url.toUri()
		startActivity(intent)
	}

	private fun getCardInfo() {
		val artistName = getArtistName()
		presenter.getArtistInfo(artistName)
	}

	private fun getArtistName(): String {
		return intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")
	}

	fun updateUI() {
		runOnUiThread {
			updateArticleText()
			updateSourceText()
		}
	}

	@SuppressLint("SetTextI18n")
	private fun updateSourceText() {
		sourceTextView.text = "Source: " + uiState.source
	}

	private fun displayLastFMImage() =
		Picasso.get().load(LASTFM_IMAGE_URL).into(logoImageView as ImageView?)

	private fun updateArticleText() {
		cardTextView.text = Html.fromHtml(uiState.infoHtml, Html.FROM_HTML_MODE_COMPACT)
	}

	private fun updateUIState(card: CardUIState) {
		uiState = uiState.copy(
			artistName = card.artistName,
			infoHtml = card.infoHtml,
			url = card.url,
			source = card.source
		)
	}

	companion object {
		const val ARTIST_NAME_EXTRA = "artistName"
	}
}