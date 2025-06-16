package ayds.songinfo.moredetails.presentation.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2
import ayds.songinfo.R
import ayds.songinfo.moredetails.injector.OtherInfoInjector
import ayds.songinfo.moredetails.presentation.CardUIState
import ayds.songinfo.moredetails.presentation.CardsUIState
import ayds.songinfo.moredetails.presentation.adapter.CardsPagerAdapter
import ayds.songinfo.moredetails.presentation.presenter.OtherInfoPresenter
import com.squareup.picasso.Picasso

class OtherInfoView : Activity() {
	var uiState: CardsUIState = CardsUIState()

	private lateinit var logoImageView: ImageView
	private lateinit var cardTextView: TextView
	private lateinit var sourceTextView: TextView
	private lateinit var openUrlButton: Button
	private lateinit var presenter: OtherInfoPresenter
	private lateinit var cardsViewPager: ViewPager2

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_other_info)

		initProperties()
		initListeners()
		initPresenter()
		initObservers()
		getCardInfo()
	}

	private fun initProperties() {
		cardsViewPager = findViewById(R.id.cardsViewPager)
	}

	private fun initListeners() {

	}

	private fun initPresenter() {
		OtherInfoInjector.init(this)
		presenter = OtherInfoInjector.presenter
	}

	private fun initObservers() {
		presenter.cardsObservable.subscribe { cards ->
			updateUIState(cards)
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

	private fun updateUI() {
		runOnUiThread {
			cardsViewPager.adapter = CardsPagerAdapter(uiState.cards) {

                navigateToURL(it)
            }
        }
	}

	private fun updateLogoSource(state: CardUIState) {
		Picasso.get().load(state.logoUrl).into(logoImageView as ImageView?)
	}

	@SuppressLint("SetTextI18n")
	private fun updateSourceText(state: CardUIState) {
		sourceTextView.text = "Source: " + state.source
	}

	private fun updateArticleText(state: CardUIState) {
		cardTextView.text = Html.fromHtml(state.infoHtml, Html.FROM_HTML_MODE_COMPACT)
	}

	private fun updateUIState(cards: CardsUIState) {
		uiState = uiState.copy(cards = cards.cards)
	}

	companion object {
		const val ARTIST_NAME_EXTRA = "artistName"
	}
}