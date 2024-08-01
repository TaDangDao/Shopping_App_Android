package com.example.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.Product
import com.example.shoppingapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SearchViewModel@Inject constructor(
    private val firestore: FirebaseFirestore
) :ViewModel(){
    private val _products= MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val fproduct=_products.asStateFlow()
    init {
        getProductsforSearch()
    }

    fun getProductsforSearch(){
        viewModelScope.launch {
            _products.emit(Resource.Loading())
        }
        firestore.collection("Products").get().addOnSuccessListener {
            viewModelScope.launch {
                val list=it.toObjects(Product::class.java)
                _products.emit(Resource.Success(list))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _products.emit(Resource.Error(it.message.toString()))
            }
        }
        }

    }
