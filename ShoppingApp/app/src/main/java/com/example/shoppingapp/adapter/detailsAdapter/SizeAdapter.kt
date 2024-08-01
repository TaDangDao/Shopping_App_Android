package com.example.shoppingapp.adapter.detailsAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.databinding.SizeRvItemBinding

class SizeAdapter:RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {
    inner class SizeViewHolder(private val binding: SizeRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(size:String,isSelected:Boolean){
            if(isSelected){
                binding.backgroundImage.visibility= View.VISIBLE
            }else{
                binding.backgroundImage.visibility= View.INVISIBLE
            }
            binding.tvSize.text=size
        }
        
    }

    val differCallback= object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        return SizeViewHolder(
            SizeRvItemBinding.inflate(LayoutInflater.from(parent.context))

        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
     var selectedSize=-1
    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val size=differ.currentList[position]
        holder.bind(size,selectedSize==position)
        holder.itemView.setOnClickListener {
            if (selectedSize >= 0){
                notifyItemChanged(selectedSize)}
            selectedSize = holder.adapterPosition
            notifyItemChanged(selectedSize)
            onClick?.invoke(size)
        }
    }
    var onClick:((String)->Unit)?=null
}