package com.example.shoppingapp.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.Order
import com.example.shoppingapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OrderViewModel@Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {
    private val _getorder = MutableStateFlow<Resource<MutableList<Order>>>(Resource.Unspecified())
    val getorder=_getorder.asStateFlow()
    init {
        getOrders()
    }

    private fun getOrders() {
        viewModelScope.launch {
            _getorder.emit(Resource.Loading())
        }
        firebaseFirestore.collection("users").document(auth.uid!!).collection("orders").get().addOnSuccessListener {
            val orders=it.toObjects(Order::class.java)
            viewModelScope.launch {
                _getorder.emit(Resource.Success(orders))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _getorder.emit(Resource.Error(it.message.toString()))
            }
        }
        }
    }
