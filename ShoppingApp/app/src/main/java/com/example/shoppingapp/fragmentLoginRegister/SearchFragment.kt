package com.example.shoppingapp.fragmentLoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.toLowerCase
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.adapter.BestProductAdapter
import com.example.shoppingapp.adapter.SearchAdapter
import com.example.shoppingapp.data.Product
import com.example.shoppingapp.databinding.SearchFragmentBinding
import com.example.shoppingapp.util.HorizontalItemDecoration
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.search_fragment)  {
    private lateinit var binding :SearchFragmentBinding
    private val searchAdapter by lazy { SearchAdapter() }
    private val viewModel by viewModels<SearchViewModel>()
    private var fillterdList = emptyList<Product>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= SearchFragmentBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSearchRv()
        searchAdapter.onClick={
            val b =Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_searchFragment2_to_productDetailFragment,b)
        }
        binding.searchIcon.setOnClickListener {
                getSearchProduct()
        }
        lifecycleScope.launch {
            viewModel.fproduct.collect{
                when(it){
                    is Resource.Loading->{
                        binding.NotFound.visibility=View.VISIBLE
                    }
                    is  Resource.Success->{
                        binding.NotFound.visibility=View.GONE
                        searchAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error->{
                        binding.NotFound.visibility=View.VISIBLE
                    }
                    else->Unit
                }
            }
        }


    }
    fun getSearchProduct(){
        if(searchAdapter.differ.currentList.isNotEmpty()){
            val text=binding.edSearch.text.toString()
            val filtereList = searchAdapter.differ.currentList.filter {
                it.name.contains(text, ignoreCase = true)
            }
            fillterdList=filtereList

        }
        searchAdapter.setFilteredList(fillterdList)
    }
    fun setUpSearchRv(){
        binding.RvSearch.apply {
            layoutManager=GridLayoutManager(requireContext(), 2,GridLayoutManager.VERTICAL,false)
            adapter=searchAdapter
            addItemDecoration(HorizontalItemDecoration())
        }

    }
}