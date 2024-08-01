package com.example.shoppingapp.fragmentLoginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.adapter.BestProductAdapter
import com.example.shoppingapp.adapter.CategoryAdapter
import com.example.shoppingapp.adapter.SpecialProductAdapter
import com.example.shoppingapp.data.Category
import com.example.shoppingapp.data.Product
import com.example.shoppingapp.databinding.BaseShoppingFragmentBinding
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.viewmodel.BaseShoppingViewModel
import com.example.shoppingapp.viewmodel.factory.BaseCategoryViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BaseShoppingFragment(val category: Category) :Fragment(){
    private lateinit var binding: BaseShoppingFragmentBinding
    private val bestProductAdapter by lazy { BestProductAdapter() }
    private val specialProductAdapter by lazy { SpecialProductAdapter() }
    @Inject
    lateinit var firestore:FirebaseFirestore
    val viewModel by viewModels<BaseShoppingViewModel> {
        BaseCategoryViewModelFactory(firestore,category)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=BaseShoppingFragmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewModel.bestProduct.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.bestProductsProgressBar.visibility=View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.bestProductsProgressBar.visibility=View.INVISIBLE
                        binding.offerProductsProgressBar.visibility=View.INVISIBLE
                        bestProductAdapter.differ.submitList(it.data)


                    }
                    is Resource.Error -> {
                        binding.bestProductsProgressBar.visibility=View.INVISIBLE
                        binding.offerProductsProgressBar.visibility=View.INVISIBLE
                    }
                    else->Unit
                }
            }
        }
        lifecycleScope.launch {
            viewModel.specialProduct.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.offerProductsProgressBar.visibility=View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.bestProductsProgressBar.visibility=View.INVISIBLE
                        binding.offerProductsProgressBar.visibility=View.INVISIBLE
                        specialProductAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        binding.bestProductsProgressBar.visibility=View.INVISIBLE
                        binding.offerProductsProgressBar.visibility=View.INVISIBLE
                    }
                    else->Unit
                }
            }
        }
        setUpBestProductRv()
        setUpSpecialRv()
        bestProductAdapter.onClick={
            val b=Bundle().apply{putParcelable("product",it)}
            findNavController().navigate(R.id.action_homeShoppingFragment_to_productDetailFragment,b)
        }
        specialProductAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeShoppingFragment_to_productDetailFragment,b)
        }
        super.onViewCreated(view, savedInstanceState)
    }

 fun setUpBestProductRv() {
     binding.rvBestProducts.apply {
         layoutManager= GridLayoutManager(this.context,2,LinearLayoutManager.VERTICAL,false)
         adapter=bestProductAdapter
     }
    }

    fun setUpSpecialRv() {
        binding.rvOfferProducts.apply {
            layoutManager= LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
            adapter=specialProductAdapter
        }
    }

}