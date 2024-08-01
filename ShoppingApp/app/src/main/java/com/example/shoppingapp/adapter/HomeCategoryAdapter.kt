package com.example.shoppingapp.adapter
import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.data.Category
import com.example.shoppingapp.databinding.CategoryRvItemBinding

class CategoryAdapter:RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(private val binding:CategoryRvItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(category: Category,isSelected:Boolean){
            binding.apply {
                tvCategory.text = category.category
                if (isSelected){
                    itemView.findViewById<TextView>(R.id.tvCategory).setTextColor(android.graphics.Color.parseColor("#2626CA"))
                }else{
                    itemView.findViewById<TextView>(R.id.tvCategory).setTextColor(android.graphics.Color.parseColor("#000000"))
                }
            }
        }
    }
val differCallback= object : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem==newItem
    }

}
    val differ = AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var selectedAddress = 0
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
       val category=differ.currentList[position]
        holder.bind(category,selectedAddress==position)
        holder.itemView.setOnClickListener{
            if (selectedAddress >= 0)
                notifyItemChanged(selectedAddress)
            selectedAddress = holder.adapterPosition
            notifyItemChanged(selectedAddress)
            onClick?.invoke(category)

        }

    }
    var onClick:((Category)->Unit)?=null
}