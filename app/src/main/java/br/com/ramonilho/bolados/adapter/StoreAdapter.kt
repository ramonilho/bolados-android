package br.com.ramonilho.bolados.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.listener.OnItemClickListener
import br.com.ramonilho.bolados.model.Store
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.store_row.view.*

/**
 * Created by ramonhonorio on 22/07/17.
 */

class StoreAdapter(private var stores: List<Store>?, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        // Inflates the layout
        val myLayout = inflater.inflate(R.layout.store_row, parent, false)

        return StoreViewHolder(myLayout)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val store = stores!![position]

        holder.itemView.tvName.text = store.name
        holder.itemView.tvCategory.text = store.category

        val priceAverage = store.priceAverage!!
        holder.itemView.tvPriceAverage.text = "• " + ("$".repeat(priceAverage))
        holder.itemView.tvRating.text = store.rating.toString()
        holder.itemView.tvDescription.text = store.description.toString()
        holder.itemView.setOnClickListener { listener.onItemClick(store) }

        // Store Logo
        Picasso.with(holder.itemView.context)
                .load(APIUtils.BASE_URL + store.urlLogo)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.itemView.ivLogo)

        // Store ShowImage
        Picasso.with(holder.itemView.context)
                .load(APIUtils.BASE_URL + store.pictures!![1])
                .placeholder(R.mipmap.ic_launcher)
                .transform(RoundedCornersTransformation(10, 10))
                .error(R.mipmap.ic_launcher)
                .into(holder.itemView.ivShowcaseImage)

    }

    override fun getItemCount(): Int {
        return stores!!.size
    }

    fun update(lines: List<Store>) {
        this.stores = lines
        notifyDataSetChanged()
    }
}
