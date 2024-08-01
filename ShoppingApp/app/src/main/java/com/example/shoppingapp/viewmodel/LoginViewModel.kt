package com.example.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.User
import com.example.shoppingapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel@Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore

) : ViewModel(){
    private val _loginStatus= MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val loginStatus = _loginStatus.asStateFlow()


    fun Login(email:String,password:String){
        viewModelScope.launch {
           val user=User("","",email,"")
            _loginStatus.emit(Resource.Loading())
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                viewModelScope.launch {
                    _loginStatus.emit(Resource.Success(user))
                }

            }.addOnFailureListener{
                viewModelScope.launch {
                    _loginStatus.emit(Resource.Error(it.message.toString()))
                }

            }

        }

    }

    fun resetPassword(emmai:String){
        viewModelScope.launch {
            firebaseAuth.sendPasswordResetEmail(emmai)

        }
    }


}