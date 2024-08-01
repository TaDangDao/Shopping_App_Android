package com.example.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.shoppingapp.databinding.Viewpager2RvItemBinding

class ViewPager2ImageAdapter:RecyclerView.Adapter<ViewPager2ImageAdapter.ViewPagerViewHolder>() {
    inner class ViewPagerViewHolder(private val binding:Viewpager2RvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(imagePath:String){
            Glide.with(itemView).load(imagePath).into(binding.imageProductDetails)
        }
    }
    val differCallback=object: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem

        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

    }
    val differ= AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            Viewpager2RvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val image=differ.currentList[position]
        holder.bind(image)
        holder.itemView.setOnClickListener {
            onClick?.invoke(image)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
var onClick:((String)->Unit)?=null

}