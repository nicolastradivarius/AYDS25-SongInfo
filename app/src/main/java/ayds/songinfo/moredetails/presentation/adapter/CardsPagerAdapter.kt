package ayds.songinfo.moredetails.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ayds.songinfo.moredetails.presentation.CardUIState
import ayds.songinfo.R

class CardsPagerAdapter(
    private val cards: List<CardUIState>,
    private val onOpenUrl: (String) -> Unit
) : RecyclerView.Adapter<CardsPagerAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int = cards.size

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val logoImageView: ImageView = view.findViewById(R.id.logoImageView)
        private val cardTextView: TextView = view.findViewById(R.id.cardTextView)
        private val sourceTextView: TextView = view.findViewById(R.id.sourceTextView)
        private val openUrlButton: Button = view.findViewById(R.id.openUrlButton)

        fun bind(card: CardUIState) {
            cardTextView.text = card.infoHtml
            sourceTextView.text = card.source
            Picasso.get().load(card.logoUrl).into(logoImageView)
            openUrlButton.setOnClickListener { onOpenUrl(card.url) }
        }
    }
}