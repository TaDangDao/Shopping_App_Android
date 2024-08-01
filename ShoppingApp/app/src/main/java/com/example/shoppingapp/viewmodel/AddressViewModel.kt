package com.example.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.Address
import com.example.shoppingapp.firebase.FirebaseCommon
import com.example.shoppingapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel@Inject constructor(
    val firebaseCommon: FirebaseCommon,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {
    private val _address = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val address=_address.asStateFlow()

    fun addAddress(address: Address){
        viewModelScope.launch {
            _address.emit(Resource.Loading())
        }
        firestore.collection("users").document(auth.uid!!).collection("address").document().set(address).addOnSuccessListener {
            viewModelScope.launch {
                _address.emit(Resource.Success(address))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _address.emit(Resource.Error(it.message.toString()))
            }
        }
    }

}