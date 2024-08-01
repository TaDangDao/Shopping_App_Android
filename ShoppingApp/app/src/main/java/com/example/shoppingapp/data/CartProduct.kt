package com.example.shoppingapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
    val product: Product,
    val quantity:Int,
    val color: Int? = null,
    val size: String? = null,
) :Parcelable{
    constructor():this(Product(),1,null,null)

}