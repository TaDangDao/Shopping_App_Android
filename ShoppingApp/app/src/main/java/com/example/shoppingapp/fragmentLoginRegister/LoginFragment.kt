package com.example.shoppingapp.fragmentLoginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.shopping_activitiy
import com.example.shoppingapp.databinding.LoginFragmentBinding
import com.example.shoppingapp.databinding.LoginRegisterFragmentBinding
import com.example.shoppingapp.dialog.setupBottomSheetDialog
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.viewmodel.LoginViewModel
import com.example.shoppingapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.login_fragment) {
    private lateinit var binding:LoginFragmentBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=LoginFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonLoginLogin.setOnClickListener {
            val email=binding.emailinput.text.toString()
            val pass=binding.passinput.text.toString()
            viewModel.Login(email,pass)
        }
        binding.resetpass.setOnClickListener {
            setupBottomSheetDialog {   email->
                viewModel.resetPassword(email)
            }

        }
        lifecycleScope.launch {
            viewModel.loginStatus.collect{
                when(it){
                    is Resource.Loading->{
                        binding.buttonLoginLogin.startAnimation()
                    }
                    is Resource.Success->{
                        binding.buttonLoginLogin.revertAnimation()
                       Intent(requireActivity(),shopping_activitiy::class.java).also {intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                           startActivity(intent)
                        }


                    }

                  is Resource.Error-> {
                      Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                  }
                    else->Unit

                }

            }

        }
    }
}