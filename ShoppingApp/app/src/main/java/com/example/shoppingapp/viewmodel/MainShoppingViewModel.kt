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
class MainShoppingViewModel@Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth

):ViewModel() {
    private val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProduct = _bestProduct.asStateFlow()
    private val _bestDealProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealProduct = _bestDealProduct.asStateFlow()
    private val _specialProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val sepcialProdcut = _specialProduct.asStateFlow()
    init {
        getBestProduct()
        getBestDealProduct()
        getSpecialProduct()
    }

    private fun getSpecialProduct() {

        firestore.collection("Products").whereEqualTo("type","special").get().addOnSuccessListener {
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

    private fun getBestDealProduct() {
        firestore.collection("Products").whereEqualTo("type","bestdeal").get()
            .addOnSuccessListener {
                viewModelScope.launch {
                    val list=it.toObjects(Product::class.java)
                    _bestDealProduct.emit(Resource.Success(list))
                }
        }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProduct.emit(Resource.Error(it.message.toString()))
                }
        }
    }


    private fun getBestProduct() {
        firestore.collection("Products").whereEqualTo("type","best").get()
            .addOnSuccessListener {
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



}