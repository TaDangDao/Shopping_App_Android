package com.example.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.example.shoppingapp.data.Product
import com.example.shoppingapp.databinding.SpecialRvItemBinding

class SpecialProductAdapter:RecyclerView.Adapter<SpecialProductAdapter.SpecialProductViewHolder>() {
    inner class SpecialProductViewHolder(private val binding: SpecialRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                tvSpecialProductName.text = product.name
                tvSpecialPrdouctPrice.text = product.price.toString()
                Glide.with(itemView).load(product.images[0]).into(imageSpecialRvItem)

            }
        }
    }
    val diffCallback=object: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }

    }
    val differ= AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductViewHolder {
        return SpecialProductViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SpecialProductViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener{
            onClick?.invoke(product)
        }
    }
    var onClick:((Product)->Unit)?=null
}