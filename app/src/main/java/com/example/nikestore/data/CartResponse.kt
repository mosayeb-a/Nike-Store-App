package com.example.nikestore.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CartResponse(
    val cart_items: List<CartItem>,
    val payable_price: Int,
    val shipping_cost: Int,
    val total_price: Int,
)

@Parcelize
data class PurchaseDetail(
    var totalPrice: Int ,
    var shipping_cost: Int,
    var payable_price: Int ,
) : Parcelable