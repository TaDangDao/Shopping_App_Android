package com.example.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.Category
import com.example.shoppingapp.data.Product
import com.example.shoppingapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BaseShoppingViewModel  constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category

) : ViewModel() {
    private val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProduct = _bestProduct.asStateFlow()
    private val _specialProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProduct = _specialProduct.asStateFlow()
    init {
        getOfferProduct()
        getSpecialProduct()
    }

    private fun getOfferProduct() {
        viewModelScope.launch {
            _bestProduct.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("type","best").whereEqualTo("category",category.category).get().addOnSuccessListener {
            viewModelScope.launch {
            val list=it.toObjects(Product::class.java)
            _bestProduct.emit(Resource.Success(list))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
            _bestProduct.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    private fun getSpecialProduct() {
        viewModelScope.launch {
            _specialProduct.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("type","special").whereEqualTo("category",category.category).get().addOnSuccessListener {
            viewModelScope.launch {
                val list=it.toObjects(Product::class.java)
                _specialProduct.emit(Resource.Success(list))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _specialProduct.emit(Resource.Error(it.message.toString()))
            }
        }
    }

}