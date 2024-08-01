package com.example.shoppingapp.fragmentLoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.adapter.detailsAdapter.CartAdapter
import com.example.shoppingapp.databinding.OrderDetailFragmentBinding
import com.example.shoppingapp.util.HorizontalItemDecoration

class OrderDetailFragment: Fragment() {
private lateinit var binding:OrderDetailFragmentBinding
private val args by navArgs<OrderDetailFragmentArgs>()
private val allProductAdapter by lazy { CartAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=OrderDetailFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val order=args.order
        allProductAdapter.differ.submitList(order.products)
        setUpOrderRv()
        binding.apply{
            tvOrderId.text="Order #${order.orderId}"
            tvFullName.text=order.address.fullName
            tvAddress.text="${order.address.street} ${order.address.city}"
            tvPhoneNumber.text=order.address.phone
            tvTotalPrice.text="$ ${order.totalPrice}"
        }
        binding.imageCloseOrder.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    private fun setUpOrderRv() {
        binding.rvProducts.apply{
            layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
            adapter = allProductAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

}