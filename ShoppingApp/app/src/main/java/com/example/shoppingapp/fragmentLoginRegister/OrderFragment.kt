package com.example.shoppingapp.fragmentLoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.adapter.OrderAdapter
import com.example.shoppingapp.databinding.OrderFragmentBinding
import com.example.shoppingapp.util.HorizontalItemDecoration
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment: Fragment() {
    private lateinit var binding:OrderFragmentBinding
    private val viewModel by viewModels<OrderViewModel>()
    private val orderAdapter by lazy { OrderAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=OrderFragmentBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpOrderRv()
        binding.imageCloseOrders.setOnClickListener {
            findNavController().navigateUp()
        }
        orderAdapter.onClick={
            val bundle=Bundle().apply {
                putParcelable("order",it)
            }
            findNavController().navigate(R.id.action_orderFragment_to_orderDetailFragment,bundle)
        }
        lifecycleScope.launch {
            viewModel.getorder.collect{
                when(it){
                    is Resource.Loading->{
                        binding.progressbarAllOrders.visibility=View.VISIBLE
                    }
                    is Resource.Success->{
                        binding.progressbarAllOrders.visibility=View.INVISIBLE
                        orderAdapter.differ.submitList(it.data)
                        if(it.data.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility=View.VISIBLE
                        }

                }
                    is Resource.Error->{
                        binding.progressbarAllOrders.visibility=View.INVISIBLE

                    }
                    else->Unit
                }

            }
        }

    }

    private fun setUpOrderRv() {
        binding.rvAllOrders.apply{
            layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
            adapter=orderAdapter
            addItemDecoration(HorizontalItemDecoration())

        }
    }

}