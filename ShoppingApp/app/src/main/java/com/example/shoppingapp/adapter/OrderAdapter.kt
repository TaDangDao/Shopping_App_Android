package com.example.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.data.Order
import com.example.shoppingapp.databinding.OrderRvItemBinding

class OrderAdapter:RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    inner class OrderViewHolder(val binding: OrderRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun binding(order: Order){
            binding.tvOrderId.text=order.orderId.toString()
            binding.tvOrderDate.text=order.date
        }
    }
    val differCallback=object: DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem==newItem
        }
    }
    val differ= AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(OrderRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order=differ.currentList[position]
        holder.binding(order)
        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }

    }
    var onClick:((Order)->Unit)?=null
}