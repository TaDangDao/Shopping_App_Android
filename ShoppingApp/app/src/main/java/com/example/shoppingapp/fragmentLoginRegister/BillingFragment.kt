package com.example.shoppingapp.fragmentLoginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.adapter.AddressAdapter
import com.example.shoppingapp.adapter.BillingProductAdapter
import com.example.shoppingapp.data.Address
import com.example.shoppingapp.data.Order
import com.example.shoppingapp.data.OrderStatus
import com.example.shoppingapp.databinding.BillingFragmentBinding
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.util.VerticalItemDecoration
import com.example.shoppingapp.viewmodel.BillingViewModel
import com.example.shoppingapp.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment: Fragment(R.layout.billing_fragment) {
    private lateinit var binding:BillingFragmentBinding
    private val  args: BillingFragmentArgs by navArgs()
    private val viewModel by viewModels<BillingViewModel>()
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductAdapter by lazy { BillingProductAdapter() }
    private var selectedAddress: Address?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= BillingFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cart=args.cart
        val price=args.price
        setUpAddressRv()
        setUpBillingProductsRv()
        addressAdapter.onClick={
            selectedAddress=it
        }
        binding.tvTotalPrice.text="$ $price"
        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonPlaceOrder.setOnClickListener {
            viewModel.addOrder(Order(OrderStatus.Ordered.status,price,cart.toList(),selectedAddress!!))

        }
        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        lifecycleScope.launch {
            billingProductAdapter.differ.submitList(cart.toList())
        }
        lifecycleScope.launch {
            viewModel.order.collectLatest {
                when(it){
                    is Resource.Loading->{
                        binding.buttonPlaceOrder.startAnimation()
                    }
                    is Resource.Success->{
                        binding.buttonPlaceOrder.revertAnimation()
                        viewModel.deleteCart()
                        findNavController().navigate(R.id.action_billingFragment_to_orderFragment)
                    }
                    is Resource.Error->{
                        binding.buttonPlaceOrder.revertAnimation()

                    }
                    else->Unit
                }
            }

        }
        lifecycleScope.launch {
            viewModel.address.collectLatest {
                when(it){
                    is Resource.Loading->{
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Resource.Success->{
                        binding.progressbarAddress.visibility=View.GONE
                        addressAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error->{
                        binding.progressbarAddress.visibility=View.GONE
                    }
                    else->Unit
                }
            }
        }



        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpAddressRv() {
        binding.rvAddress.apply {
            layoutManager= LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
            adapter=addressAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }

    private fun setUpBillingProductsRv() {
        binding.rvProducts.apply {
            layoutManager= LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
            adapter=billingProductAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }

}