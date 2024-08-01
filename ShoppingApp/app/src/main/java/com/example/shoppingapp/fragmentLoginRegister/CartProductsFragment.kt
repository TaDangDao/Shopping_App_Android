package com.example.shoppingapp.fragmentLoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.adapter.detailsAdapter.CartAdapter
import com.example.shoppingapp.databinding.CartProductFragmentBinding
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.util.VerticalItemDecoration
import com.example.shoppingapp.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartProductsFragment: Fragment() {
    private lateinit var binding: CartProductFragmentBinding
    private val viewModel by viewModels<CartViewModel>()
    private val cartAdapter by lazy { CartAdapter() }
    var totalPrice=0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=CartProductFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCartRv()
        var totalPrice = 0f
        lifecycleScope.launch {
            viewModel.productsPrice.collectLatest { price ->
                price?.let {
                    totalPrice = it
                    binding.tvTotalPrice.text = "$ $totalPrice"
                }
            }
        }

        lifecycleScope.launch {
            viewModel.cartProducts.collect{
                when(it){
                    is Resource.Loading->{
                        binding.progressbarCart.visibility=View.VISIBLE
                    }
                    is Resource.Success->{
                        binding.progressbarCart.visibility=View.INVISIBLE
                        cartAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error->{
                        binding.progressbarCart.visibility=View.INVISIBLE
                    }
                    else->Unit
                }

            }
        }
        cartAdapter.apply {
            onMinusClick = {
                viewModel.changeQuantity(it,"decrease")
            }
            onPlusClick = {
               viewModel.changeQuantity(it,"increase")
            }
            onClick = {
                val b = Bundle().apply { putParcelable("product", it.product) }
                findNavController().navigate(R.id.action_cartProductsFragment_to_productDetailFragment, b)
            }
        }

        binding.imageCloseCart.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonCheckout.setOnClickListener {
            val action=CartProductsFragmentDirections.actionCartProductsFragmentToBillingFragment(cartAdapter.differ.currentList.toTypedArray(),totalPrice)
            findNavController().navigate(action)
        }

    }
    fun calculateTotalPrice(){
        cartAdapter.differ.currentList.forEach {
            if(it.product.offerPercentage!=null){
                totalPrice+=(it.product.price-it.product.price*it.product.offerPercentage)*it.quantity
            }
            else{
                totalPrice+=it.product.price*it.quantity
            }
        }
    }

    private fun setUpCartRv() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter=cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }

    }
}