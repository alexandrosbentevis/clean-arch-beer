package com.alexandrosbentevis.beer.features.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.alexandrosbentevis.beer.R
import com.alexandrosbentevis.beer.databinding.ItemBeerBinding
import com.alexandrosbentevis.beer.extensions.autoNotify
import com.alexandrosbentevis.beer.extensions.load
import com.alexandrosbentevis.beer.extensions.setSafeOnClickListener
import kotlin.properties.Delegates

/**
 * Adapter for the beers recycler view.
 */
internal class BeerAdapter(
    private val action: (BeerUiModel, ImageView) -> Unit
) : RecyclerView.Adapter<BeerAdapter.BeerViewHolder>() {

    var items: List<BeerUiModel> by Delegates.observable(emptyList()) { _, oldList, newList ->
        autoNotify(oldList, newList) { o, n -> o.id == n.id }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerAdapter.BeerViewHolder =
        BeerViewHolder(ItemBeerBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: BeerAdapter.BeerViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class BeerViewHolder(private val binding: ItemBeerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BeerUiModel) {

            ViewCompat.setTransitionName(binding.beerImageView, item.id)
            binding.root.setSafeOnClickListener {
                action(item, binding.beerImageView)
            }
            binding.beerNameTextView.text = item.name
            binding.beerAbvTextView.text = binding.root.context.getString(R.string.abv, item.abv)
            binding.beerTaglineTextView.text = item.tagline
            binding.beerImageView.load(
                url = item.imageUrl
            )
        }
    }
}