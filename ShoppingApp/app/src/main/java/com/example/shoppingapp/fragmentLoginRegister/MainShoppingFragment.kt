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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.adapter.BestDealProductAdapter
import com.example.shoppingapp.adapter.BestProductAdapter
import com.example.shoppingapp.adapter.SpecialProductAdapter
import com.example.shoppingapp.databinding.MainShoppingFragmentBinding
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.viewmodel.MainShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainShoppingFragment: Fragment(R.layout.main_shopping_fragment) {
    private lateinit var binding: MainShoppingFragmentBinding
    private val viewModel by viewModels<MainShoppingViewModel>()
    private val bestDealProductAdapter by lazy { BestDealProductAdapter() }
    private lateinit var bestProductAdapter: BestProductAdapter
    private val specialProductAdapter by lazy { SpecialProductAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainShoppingFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBestProductRv()
        setUpBestDealRv()
        setUpSpecialRv()
        bestProductAdapter.onClick={
            val b=Bundle().apply{putParcelable("product",it)}
            findNavController().navigate(R.id.action_homeShoppingFragment_to_productDetailFragment,b)
        }
        specialProductAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeShoppingFragment_to_productDetailFragment,b)
        }
        bestDealProductAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeShoppingFragment_to_productDetailFragment,b)
        }

        lifecycleScope.launch {
            viewModel.bestDealProduct.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.mainCategoryProgressbar.visibility = View.VISIBLE
                        binding.bestProductsProgressbar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        bestDealProductAdapter.differ.submitList(it.data)
                        binding.mainCategoryProgressbar.visibility = View.INVISIBLE
                        binding.bestProductsProgressbar.visibility = View.INVISIBLE
                    }

                    is Resource.Error -> {
                        binding.mainCategoryProgressbar.visibility = View.INVISIBLE
                        binding.bestProductsProgressbar.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit

                }
            }
        }
        lifecycleScope.launch {
            viewModel.bestProduct.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.mainCategoryProgressbar.visibility = View.VISIBLE
                        binding.bestProductsProgressbar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.mainCategoryProgressbar.visibility = View.INVISIBLE
                        binding.bestProductsProgressbar.visibility = View.INVISIBLE
                        bestProductAdapter.differ.submitList(it.data)

                    }

                    is Resource.Error -> {}
                    else -> Unit

                }
            }
        }
        lifecycleScope.launch {
            viewModel.sepcialProdcut.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.mainCategoryProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.mainCategoryProgressbar.visibility = View.INVISIBLE
                        specialProductAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        binding.mainCategoryProgressbar.visibility = View.INVISIBLE

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit

                }
            }
        }

    }

    fun setUpBestProductRv(){
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }
    fun setUpBestDealRv(){

        binding.rvBestDealsProducts.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = bestDealProductAdapter
        }
    }

    fun setUpSpecialRv(){
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = specialProductAdapter
        }

    }
}