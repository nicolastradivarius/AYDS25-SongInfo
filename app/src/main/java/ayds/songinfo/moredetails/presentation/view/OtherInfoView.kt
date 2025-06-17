package ayds.songinfo.moredetails.presentation.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2
import ayds.songinfo.R
import ayds.songinfo.moredetails.injector.OtherInfoInjector
import ayds.songinfo.moredetails.presentation.CardsUIState
import ayds.songinfo.moredetails.presentation.view.adapter.CardsPagerAdapter
import ayds.songinfo.moredetails.presentation.presenter.OtherInfoPresenter

class OtherInfoView : Activity() {
	var uiState: CardsUIState = CardsUIState()

	private lateinit var presenter: OtherInfoPresenter
	private lateinit var cardsViewPager: ViewPager2

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_other_info)

		initProperties()
		initPresenter()
		initObservers()
		getCardInfo()
	}

	private fun initProperties() {
		cardsViewPager = findViewById(R.id.cardsViewPager)
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

	private fun updateUIState(cards: CardsUIState) {
		uiState = uiState.copy(cards = cards.cards)
	}

	companion object {
		const val ARTIST_NAME_EXTRA = "artistName"
	}
}