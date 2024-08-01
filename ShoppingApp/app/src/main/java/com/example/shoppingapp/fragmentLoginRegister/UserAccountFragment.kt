package com.example.shoppingapp.fragmentLoginRegister

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.shoppingapp.data.User
import com.example.shoppingapp.databinding.AccountFragmentBinding
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.viewmodel.UserAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class UserAccountFragment: Fragment() {
    private lateinit var binding: AccountFragmentBinding
    private val viewModel by viewModels<UserAccountViewModel>()
    private var user: User? = null
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                imageUri = it.data?.data
                Glide.with(this).load(imageUri).into(binding.imageUser)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AccountFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSave.setOnClickListener {
            binding.apply {
                val firstName = edFirstName.text.toString().trim()
                val lastName = edLastName.text.toString().trim()
                val email = edEmail.text.toString().trim()
                val user = User(firstName, lastName, email)
                viewModel.updateUser(user, imageUri)
            }
        }
        binding.imageCloseUserAccount.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.imageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(intent)
        }
        lifecycleScope.launch {
            viewModel.updateInfo.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonSave.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonSave.revertAnimation()
                        findNavController().navigateUp()
                    }
                    is Resource.Error -> {
                        binding.buttonSave.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launch {
            viewModel.user.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAccount.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAccount.visibility = View.GONE
                        user = it.data
                        showinFor(it.data!!)
                    }

                    is Resource.Error -> {
                        binding.progressbarAccount.visibility = View.INVISIBLE
                    }

                    else -> Unit
                }
            }
        }

    }

    fun showinFor(user: User) {
        binding.apply {
            Glide.with(this@UserAccountFragment).load(user.imgpath).error(ColorDrawable(0)).into(imageUser)
            edEmail.setText(user.email)
            edFirstName.setText(user.fName)
            edLastName.setText(user.lName)
        }
    }

    fun hideinfor() {
        binding.apply {
            edEmail.visibility = View.GONE
            edFirstName.visibility = View.GONE
            edLastName.visibility = View.GONE
        }
    }
}