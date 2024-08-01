package com.example.shoppingapp.adapter.detailsAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.databinding.ColorRvItemBinding
import java.util.zip.Inflater

class ColorAdapter: RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {
    inner class ColorViewHolder(private val binding: ColorRvItemBinding):RecyclerView.ViewHolder(binding.root){
          fun bind(Color:Int,isSelected:Boolean){
              if(isSelected){
                  binding.imageShadow.visibility= View.VISIBLE
                  binding.imagePicked.visibility= View.VISIBLE
              }else{
                  binding.imageShadow.visibility= View.INVISIBLE
                  binding.imagePicked.visibility= View.INVISIBLE
              }
              binding.imageColor.setBackgroundColor(Color)
          }
    }
    val differCallback=object: DiffUtil.ItemCallback<Int>(){
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem==newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(
            ColorRvItemBinding.inflate(LayoutInflater.from(parent.context))

        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
var selectedColor=-1
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color=differ.currentList[position]
        holder.bind(color,selectedColor==position)
        holder.itemView.setOnClickListener {
            if(selectedColor>=0){
                notifyItemChanged(selectedColor)
            }
            selectedColor=holder.adapterPosition
            notifyItemChanged(selectedColor)
            onClick?.invoke(color)
        }
    }
    var onClick:((Int)->Unit)?=null
}