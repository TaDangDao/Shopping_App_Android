package com.example.shoppingapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong

@Parcelize
data class Order(
    val orderStatus: String = "",
    val totalPrice: Float = 0f,
    val products: List<CartProduct> = emptyList(),
    val address: Address = Address(),
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
    val orderId: Long = nextLong(0,100_000_000_000) + totalPrice.toLong()
): Parcelable{
    constructor() :this("",0f, emptyList(),Address(),"",0)
}