package com.example.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.data.Product
import com.example.shoppingapp.databinding.BestProductRvItemBinding

class BestProductAdapter: RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {
    inner class BestProductViewHolder(private val binding:BestProductRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            val price=product.price-(product.price*product.offerPercentage!!)
            binding.apply {
                tvName.text=product.name
                tvPrice.text=product.price.toString()
                tvNewPrice.text=price.toString()
                Glide.with(itemView).load(product.images[0]).into(imgProduct)

            }
        }
    }
    val differCallback=object: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }

    }
    val differ= AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(
            BestProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener{
            onClick?.invoke(product)
        }

    }
    var onClick:((Product)->Unit)?=null
}