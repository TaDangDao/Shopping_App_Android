package com.example.shoppingapp.fragmentLoginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.adapter.BestProductAdapter
import com.example.shoppingapp.adapter.ViewPager2ImageAdapter
import com.example.shoppingapp.adapter.detailsAdapter.ColorAdapter
import com.example.shoppingapp.adapter.detailsAdapter.SizeAdapter
import com.example.shoppingapp.data.CartProduct
import com.example.shoppingapp.data.Product
import com.example.shoppingapp.databinding.ProductDetailFragmentBinding
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.viewmodel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment:Fragment() {
    private lateinit var binding:ProductDetailFragmentBinding
    private val viewModel by viewModels<ProductDetailViewModel>()
    private val adapterColor = ColorAdapter()
    private val adapterSize= SizeAdapter()
    private val imageAdapter by lazy { ViewPager2ImageAdapter() }
    private val args by navArgs<ProductDetailFragmentArgs>()
    private var selectedColor:Int?=null
    private var selectedSize:String?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ProductDetailFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product=args.product
        showInfor(product)
        setUpColorRv(product)
        setUpSizeRv(product)
        setUpViewPager(product)
        closeFragment()
        adapterColor.onClick={
            selectedColor=it
        }
        adapterSize.onClick={
            selectedSize=it}
        binding.buttonAddToCart.setOnClickListener {
                addtoCart(product)
        }
        lifecycleScope.launch {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading->{
                        binding.buttonAddToCart.startAnimation()
                    }
                    is Resource.Success->{
                        binding.buttonAddToCart.revertAnimation()
//                        val b=Bundle().apply { putParcelable("cart",it.data) }
//                        findNavController().navigate(R.id.action_productDetailFragment_to_cartProductsFragment,b)
                          findNavController().navigateUp()
                    }
                    is Resource.Error->{
                        binding.buttonAddToCart.revertAnimation()
                        Toast.makeText(requireContext(),"${it.message}",Toast.LENGTH_SHORT).show()
                    }
                    else->Unit

                }

            }

        }
    }

    private fun setUpViewPager(product:Product) {
        binding.viewPagerProductImages.apply {
            product.images?.let { imageAdapter.differ.submitList(it) }
            adapter=imageAdapter
        }
    }

    fun showInfor(product:Product){
        var price=product.price
        if(product.offerPercentage!=null){
        price=product.price-(product.price*product.offerPercentage!!)}
        binding.apply {
            tvProductName.text=product.name
            tvProductDescription.text=product.description
            tvProductPrice.text=(price).toString()
        }

    }

    private fun setUpColorRv(product:Product) {
        binding.rvColors.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            product.colors?.let { adapterColor.differ.submitList(it) }
            adapter=adapterColor
        }
    }

    private fun setUpSizeRv(product:Product){
        binding.rvSizes.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            product.sizes?.let { adapterSize.differ.submitList(it) }
            adapter=adapterSize
        }
    }

    fun addtoCart(product:Product){

        viewModel.UpdateAdddCartProduct(
                CartProduct(
                   product,1,selectedColor,selectedSize)
            )
    }
    fun closeFragment(){
        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}