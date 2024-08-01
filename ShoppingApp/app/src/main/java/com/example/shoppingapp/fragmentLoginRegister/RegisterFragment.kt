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
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.RegisterFragmentBinding
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment: Fragment() {
    private lateinit var binding: RegisterFragmentBinding
    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            binding=RegisterFragmentBinding.inflate(inflater)
            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonRegisRegister.setOnClickListener {
        val fname=binding.fnameinput.text.toString()
        val snam=binding.snameinput.text.toString()
        val email=binding.emailinput.text.toString()
        val pass=binding.passinput.text.toString()
        viewModel.register(fname,snam,email,pass)
        }
        lifecycleScope.launch {
            viewModel.registerStatus.collect {
                when(it){
                    is Resource.Loading->{
                        binding.buttonRegisRegister.startAnimation()
                    }
                    is Resource.Success->{
                        binding.buttonRegisRegister.revertAnimation()
                        findNavController().navigate(R.id.action_registerFragment2_to_loginFragment2)
                    }
                    is Resource.Error->{
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()

                    }
                    else->Unit

                }

            }

        }

    }

}