package com.example.shoppingapp.fragmentLoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.LoginRegisterFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountOptionFragment: Fragment(R.layout.login_register_fragment) {
    private lateinit var binding:LoginRegisterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=LoginRegisterFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            registerbtn.setOnClickListener {
                findNavController().navigate(R.id.action_accountOptionFragment_to_registerFragment2)

            }
           loginbtnacc.setOnClickListener{

               findNavController().navigate(R.id.action_accountOptionFragment_to_loginFragment2)
           }

        }
    }
}