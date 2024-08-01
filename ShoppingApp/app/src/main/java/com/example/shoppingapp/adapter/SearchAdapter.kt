package com.example.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.data.Product
import com.example.shoppingapp.databinding.BestDealRvItemBinding

class SearchAdapter:RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    inner class SearchViewHolder(private val binding: BestDealRvItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(product: Product){
            val price=product.price-(product.price*product.offerPercentage!!)
            binding.apply {
                tvDealProductName.text = product.name
                tvNewPrice.text = price.toString()
                tvOldPrice.text = product.price.toString()
                Glide.with(itemView).load(product.images[0]).into(imgBestDeal)
            }

        }

    }
    val diffCallback = object: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }

    }
    var differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            BestDealRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick!!.invoke(product)
        }
    }

    fun setFilteredList(fillterdList: List<Product>) {
        differ.submitList(fillterdList)
        notifyDataSetChanged()

    }

    var onClick: ((Product) -> Unit)? = null
}