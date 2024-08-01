package com.example.shoppingapp.fragmentLoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.adapter.BestDealProductAdapter
import com.example.shoppingapp.adapter.BestProductAdapter
import com.example.shoppingapp.adapter.CategoryAdapter
import com.example.shoppingapp.adapter.HomeViewPager2Adapter
import com.example.shoppingapp.adapter.SpecialProductAdapter
import com.example.shoppingapp.data.Category
import com.example.shoppingapp.databinding.HomeShoppingFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class HomeShoppingFragment: Fragment(R.layout.home_shopping_fragment) {
    private lateinit var binding:HomeShoppingFragmentBinding
    private val categoryAdapter by lazy{CategoryAdapter()}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=HomeShoppingFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            categoryAdapter.differ.submitList(listOf(Category.Home,Category.Chair,Category.Accessory,Category.Cupboard,Category.Table,Category.Furniture))
            binding.recyclerView.apply {
                layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                adapter=categoryAdapter
            }
        }
        val categoryFragment= arrayListOf(
            MainShoppingFragment(),
            BaseShoppingFragment(Category.Chair),
            BaseShoppingFragment(Category.Accessory),
            BaseShoppingFragment(Category.Cupboard),
            BaseShoppingFragment(Category.Table),
            BaseShoppingFragment(Category.Furniture)
        )
        binding.viewPager2.isUserInputEnabled = false

        val viewPager2Adapter = HomeViewPager2Adapter(categoryFragment, childFragmentManager, lifecycle)
        binding.viewPager2.adapter = viewPager2Adapter
        categoryAdapter.onClick={

            when(it){
                is Category.Home->{
                    binding.viewPager2.setCurrentItem(0)
                }
                is Category.Chair->{
                    binding.viewPager2.setCurrentItem(1)
                }
                is Category.Accessory->{
                    binding.viewPager2.setCurrentItem(2)
                }
                is Category.Cupboard->{ binding.viewPager2.setCurrentItem(3)
                }
                is Category.Table->{ binding.viewPager2.setCurrentItem(4)
                }
                is Category.Furniture->{ binding.viewPager2.setCurrentItem(5)
                }
            }
        }


    }
}