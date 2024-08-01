package com.example.shoppingapp.adapter.detailsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.R
import com.example.shoppingapp.data.CartProduct
import com.example.shoppingapp.databinding.CartRvItemBinding

class CartAdapter:RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    inner class CartViewHolder(val binding: CartRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(cartProduct: CartProduct){
            var percent=0f
            if(cartProduct.product.offerPercentage!=null){
                percent=cartProduct.product.offerPercentage
            }
            val priceAfterOffer=cartProduct.product.price*(1f-percent)
            binding.apply {
                tvProductCartName.text=cartProduct.product.name
                tvProductCartPrice.text=priceAfterOffer.toString()
                tvCartProductSize.text=cartProduct.size.toString()
                tvCartProductQuantity.text=cartProduct.quantity.toString()
                Glide.with(itemView).load(cartProduct.product.images[0]).into(imageCartProduct)
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
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
       return CartViewHolder(
           CartRvItemBinding.inflate(
               LayoutInflater.from(parent.context)
           )
       )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartProduct=differ.currentList[position]
        holder.bind(cartProduct)
        holder.apply {
           itemView.findViewById<ImageView>(R.id.imageMinus).setOnClickListener {
               onMinusClick?.invoke(cartProduct)
            }
            itemView.findViewById<ImageView>(R.id.imagePlus).setOnClickListener {
               onPlusClick?.invoke(cartProduct)
            }
            itemView.findViewById<ImageView>(R.id.imageCartProduct).setOnClickListener {
                onClick?.invoke(cartProduct)
            }
        }
    }
    var onClick:((CartProduct)->Unit)?=null
    var onMinusClick:((CartProduct)->Unit)?=null
    var onPlusClick:((CartProduct)->Unit)?=null
}