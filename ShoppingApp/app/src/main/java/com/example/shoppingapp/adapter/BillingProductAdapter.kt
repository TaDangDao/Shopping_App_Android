package com.example.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.data.CartProduct
import com.example.shoppingapp.databinding.BillingProductsRvItemBinding

class BillingProductAdapter: RecyclerView.Adapter<BillingProductAdapter.BillingProductViewHolder>() {
    inner class BillingProductViewHolder(private val binding: BillingProductsRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(cart: CartProduct){
            binding.apply {
                tvProductCartName.text=cart.product.name
                var percent=0f
                if(cart.product.offerPercentage!=null){
                    percent=cart.product.offerPercentage
                }
                val priceAfterOffer=cart.product.price*(1f-percent)
                tvProductCartPrice.text=priceAfterOffer.toString()
                tvBillingProductQuantity.text=cart.quantity.toString()
                Glide.with(itemView).load(cart.product.images[0]).into(imageCartProduct)
                if(cart.color!=null){
                imageCartProductColor.setBackgroundColor(cart.color)
                }else{
                    imageCartProductColor.visibility= View.INVISIBLE
                }
                if(cart.size!=null){
                   tvCartProductSize.setText(cart.size.toString())
                }else{
                    imageCartProductColor.visibility= View.INVISIBLE
                }
            }
        }
    }
    val differCallback=object: DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem==newItem
        }

    }
    val differ= AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductViewHolder {
      return BillingProductViewHolder(
          BillingProductsRvItemBinding.inflate(
              LayoutInflater.from(parent.context)
          )
      )
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BillingProductViewHolder, position: Int) {
        val cart = differ.currentList[position]
        holder.bind(cart)

    }
}