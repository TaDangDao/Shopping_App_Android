package com.example.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.User
import com.example.shoppingapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel@Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore

):ViewModel() {
    private val _registerStatus= MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val registerStatus:MutableStateFlow<Resource<User>> = _registerStatus


    fun register(fname:String,sname:String,email:String,password:String){
        val user=User(fname,sname,email)
        viewModelScope.launch {
            _registerStatus.emit(Resource.Loading())
        }

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            viewModelScope.launch {
                _registerStatus.emit(Resource.Success(user))
                firestore.collection("users").document(firebaseAuth.uid!!).set(user)
            }


        }.addOnFailureListener {
            viewModelScope.launch {
                _registerStatus.emit(Resource.Error(it.message.toString()))
            }
        }


    }



}

