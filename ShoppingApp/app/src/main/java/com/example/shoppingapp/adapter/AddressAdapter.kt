package com.example.shoppingapp.adapter

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.data.Address
import com.example.shoppingapp.databinding.AddressRvItemBinding

class AddressAdapter: RecyclerView.Adapter<AddressAdapter.AddressViewHolder> (){

    inner class AddressViewHolder( val binding: AddressRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(address: Address, isSelectedAddress:Boolean){
            binding.apply {
                buttonAddress.text = address.addressTitle
                if (isSelectedAddress) {
                    buttonAddress.background =
                        ColorDrawable(itemView.context.resources.getColor(R.color.g_blue))
                } else {
                    buttonAddress.background =
                        ColorDrawable(itemView.context.resources.getColor(R.color.g_white))
                }
            }


        }
    }

    val differCallback= object : DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle == newItem.addressTitle && oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }
    init {
        differ.addListListener { _, _ ->
            notifyItemChanged(selectedAddress)
        }
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }
    var selectedAddress=-1
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address=differ.currentList[position]
        holder.bind(address,selectedAddress==position)
        holder.binding.buttonAddress.setOnClickListener {
            if (selectedAddress >= 0){
                notifyItemChanged(selectedAddress)
            }
            selectedAddress = holder.adapterPosition
            notifyItemChanged(selectedAddress)
            onClick?.invoke(address)
        }
    }
    var onClick:((Address)->Unit)?=null
}