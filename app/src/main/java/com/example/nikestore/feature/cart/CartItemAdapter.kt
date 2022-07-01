package com.example.nikestore.feature.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.formatPrice
import com.example.nikestore.data.CartItem
import com.example.nikestore.data.PurchaseDetail
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.view.NikeImageView


const val VIEW_TYPE_CART_ITEM = 0
const val VIEW_TYPE_PURCHASE_DETAILS = 1

class CartItemAdapter(
    val cartItems: MutableList<CartItem>,
    val imageLoadingServiceInterface: ImageLoadingService,
    val cartItemViewCallbacks: CartItemViewCallbacks,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var purchaseDetail: PurchaseDetail? = null

    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productTitleTv: TextView = itemView.findViewById(R.id.productTitleTv)
        private val cartItemCountTv: TextView = itemView.findViewById(R.id.cartItemCountTv)
        private val previousPriceTv: TextView = itemView.findViewById(R.id.previousPriceTv)
        private val priceTv: TextView = itemView.findViewById(R.id.priceTv)
        private val productIv: NikeImageView = itemView.findViewById(R.id.productIv)
        private val removeFromCartBtn: TextView = itemView.findViewById(R.id.removeFromCartBtn)
        private val increaseBtn: ImageView = itemView.findViewById(R.id.increaseBtn)
        private val decreaseBtn: ImageView = itemView.findViewById(R.id.decreaseBtn)
        private val changeCountProgressBar: ProgressBar =
            itemView.findViewById(R.id.changeCountProgressBar)

        fun bindCartItem(cartItem: CartItem) {
            productTitleTv.text = cartItem.product.title
            cartItemCountTv.text = cartItem.count.toString()
            previousPriceTv.text = formatPrice(cartItem.product.price + cartItem.product.discount)
            priceTv.text = formatPrice(cartItem.product.price)
            imageLoadingServiceInterface.load(productIv, cartItem.product.image)
            removeFromCartBtn.setOnClickListener {
                cartItemViewCallbacks.onRemoveCartItemButtonClick(cartItem)
            }
            changeCountProgressBar.visibility =
                if (cartItem.changeCountProgressBarIsVisible) View.VISIBLE else View.GONE

            cartItemCountTv.visibility =
                if (cartItem.changeCountProgressBarIsVisible) View.INVISIBLE else View.VISIBLE

            increaseBtn.setOnClickListener {
                cartItem.changeCountProgressBarIsVisible = true
                changeCountProgressBar.visibility = View.VISIBLE
                cartItemCountTv.visibility = View.INVISIBLE
                cartItemViewCallbacks.onIncreaseCartItemButtonClick(cartItem)
            }
            decreaseBtn.setOnClickListener {
                if (cartItem.count > 1) {
                    cartItem.changeCountProgressBarIsVisible = true
                    changeCountProgressBar.visibility = View.VISIBLE
                    cartItemCountTv.visibility = View.INVISIBLE
                    cartItemViewCallbacks.onDecreaseCartItemButtonClick(cartItem)
                }
            }
            productIv.setOnClickListener {
                cartItemViewCallbacks.onProductImageClick(cartItem)
            }

            if (cartItem.changeCountProgressBarIsVisible) {
                changeCountProgressBar.visibility = View.VISIBLE
                increaseBtn.visibility = View.GONE
            }
        }
    }

    class PurchaseDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val totalPriceTv: TextView = itemView.findViewById(R.id.totalPriceTv)
        private val shippingCostTv: TextView = itemView.findViewById(R.id.shippingCostTv)
        private val payablePriceTv: TextView = itemView.findViewById(R.id.payablePriceTv)

        fun bind(totalPrice: Int, shippingCost: Int, payablePrice: Int) {
            totalPriceTv.text = formatPrice(totalPrice)
            shippingCostTv.text = formatPrice(shippingCost)
            payablePriceTv.text = formatPrice(payablePrice)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == cartItems.size)
            VIEW_TYPE_PURCHASE_DETAILS
        else
            VIEW_TYPE_CART_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CART_ITEM)
            CartItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cart, parent, false))
        else PurchaseDetailViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_purchase_details, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartItemViewHolder)
            holder.bindCartItem(cartItems[position])
        else if (holder is PurchaseDetailViewHolder) {
            purchaseDetail?.let {
                holder.bind(it.totalPrice, it.shipping_cost, it.payable_price)
            }
        }
    }

    override fun getItemCount(): Int =
        cartItems.size + 1

    fun removeCartItem(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun increaseCount(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems[index].changeCountProgressBarIsVisible = false
            notifyItemChanged(index)
        }
    }

    fun decreaseCount(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems[index].changeCountProgressBarIsVisible = false
            notifyItemChanged(index)
        }
    }

    interface CartItemViewCallbacks {
        fun onRemoveCartItemButtonClick(cartItem: CartItem)
        fun onIncreaseCartItemButtonClick(cartItem: CartItem)
        fun onDecreaseCartItemButtonClick(cartItem: CartItem)
        fun onProductImageClick(cartItem: CartItem)
    }

}