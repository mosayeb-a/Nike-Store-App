package com.example.nikestore.feature.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.formatPrice
import com.example.nikestore.common.implementSpringAnimationTrait
import com.example.nikestore.data.Product
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.view.NikeImageView

class PopularProductListAdapter(
    val imageLoadingServiceInterface: ImageLoadingService,
    ) : RecyclerView.Adapter<PopularProductListAdapter.ViewHolder>() {

    var products = ArrayList<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onPopularProductClickListener: OnPopularProductClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTv: TextView = itemView.findViewById(R.id.productTitleTv)
        private val productIv: NikeImageView = itemView.findViewById(R.id.productIv)
        private val currentPriceTv: TextView = itemView.findViewById(R.id.currentPriceTv)
        private val previousPrice: TextView = itemView.findViewById(R.id.previousPriceTv)
        private val favoriteBtn:ImageView = itemView.findViewById(R.id.favoriteBtn)


        fun bindProduct(product: Product) {
            imageLoadingServiceInterface.load(productIv, product.image)
            titleTv.text = product.title
            currentPriceTv.text = formatPrice(product.price)
            previousPrice.text = formatPrice(product.previous_price)
            previousPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                onPopularProductClickListener?.onPopularProductClick(product)
            }

            if (product.isFavorite)
                favoriteBtn.setImageResource(R.drawable.ic_favorite_fill)
            else
                favoriteBtn.setImageResource(R.drawable.ic_favorites)

            favoriteBtn.setOnClickListener {
                onPopularProductClickListener?.onPopularFavoriteBtnClick(product)

                product.isFavorite = !product.isFavorite
                notifyItemChanged(adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindProduct(products[position])

    override fun getItemCount(): Int = products.size

    interface OnPopularProductClickListener {
        fun onPopularProductClick(product: Product)
        fun onPopularFavoriteBtnClick(product: Product)
    }
}