package com.example.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.Address
import com.example.shoppingapp.data.Order
import com.example.shoppingapp.firebase.FirebaseCommon
import com.example.shoppingapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel@Inject constructor(
   val firebaseCommon: FirebaseCommon,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _address = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val address=_address.asStateFlow()
    private val _order = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val order = _order.asStateFlow()
    init {
        getUserAddresses()
    }

    fun deleteCart(){
        firebaseCommon.deleteCart()
    }

    private fun getUserAddresses() {
        viewModelScope.launch {
            _address.emit(Resource.Loading())
        }
        firestore.collection("users").document(auth.uid!!).collection("address").get().addOnFailureListener {
            viewModelScope.launch {
                _address.emit(Resource.Error(it.message.toString()))
            }

        }.addOnSuccessListener {
            viewModelScope.launch {
                val addresses=it.toObjects(Address::class.java)
                _address.emit(Resource.Success(addresses))
            }

        }

    }

    fun addOrder(orders: Order) {
        viewModelScope.launch {
            _order.emit(Resource.Loading())
        }
        firestore.collection("users").document(auth.uid!!).collection("orders").document().set(orders).addOnFailureListener {
            viewModelScope.launch {
                _order.emit(Resource.Error(it.message.toString()))
            }

        }.addOnSuccessListener {
            viewModelScope.launch {
                _order.emit(Resource.Success(orders))
            }

        }

    }

}