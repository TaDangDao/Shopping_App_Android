package com.example.shoppingapp.fragmentLoginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shoppingapp.MainActivity
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.ProfileFragmentBinding
import com.example.shoppingapp.fragmentLoginRegister.ProfileFragmentDirections.ActionProfileFragment2ToBillingFragment
import com.example.shoppingapp.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.profile_fragment) {
    private lateinit var binding:ProfileFragmentBinding
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment2_to_userAccountFragment)
        }
        binding.linearBilling.setOnClickListener {
//         val action= ProfileFragmentDirections.actionProfileFragment2ToBillingFragment(emptyArray(),0f)
//            findNavController().navigate(action)
        }
        binding.linearOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment2_to_orderFragment)
        }

        binding.linearLogOut.setOnClickListener {
            viewModel.logout()
            val intent= Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }
    }
}