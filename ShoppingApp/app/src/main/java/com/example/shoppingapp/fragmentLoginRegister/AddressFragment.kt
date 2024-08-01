package com.example.shoppingapp.fragmentLoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shoppingapp.data.Address
import com.example.shoppingapp.databinding.AddressFragmentBinding
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment: Fragment() {
    private lateinit var binding: AddressFragmentBinding
    private val viewModel by viewModels<AddressViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= AddressFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageAddressClose.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonSave.setOnClickListener {
            val name=binding.edFullName.text.toString()
            val street=binding.edStreet.text.toString()
            val addressTitle=binding.edAddressTitle.text.toString()
            val phone=binding.edPhone.text.toString()
            val city=binding.edCity.text.toString()
            val state=binding.edState.text.toString()
            viewModel.addAddress(Address(addressTitle,name,street,phone,city,state))
        }
        lifecycleScope.launch {
            viewModel.address.collectLatest {
                when(it){
                    is Resource.Loading->{
                        binding.progressbarAddress.visibility =View.VISIBLE
                    }
                    is Resource.Success->{
                        binding.progressbarAddress.visibility =View.INVISIBLE
                        findNavController().navigateUp()
                    }
                    is Resource.Error->{
                        binding.progressbarAddress.visibility =View.INVISIBLE
                    }
                    else->Unit
                }

            }
        }
    }
}