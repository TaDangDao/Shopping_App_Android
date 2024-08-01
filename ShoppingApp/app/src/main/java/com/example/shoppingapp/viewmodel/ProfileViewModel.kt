package com.example.shoppingapp.viewmodel

import android.net.Uri
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
class ProfileViewModel@Inject constructor(
    val auth: FirebaseAuth,
    val firestore: FirebaseFirestore
) : ViewModel(){
    private val _user =MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val fuser=_user.asStateFlow()

    fun getUserdata(){
        viewModelScope.launch {
            _user.emit(Resource.Loading())
        }
        firestore.collection("users").document(auth.uid!!)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    viewModelScope.launch {
                        _user.emit(Resource.Error(error.message.toString()))
                    }
                } else {
                    val user = value?.toObject(User::class.java)
                    user?.let {
                        viewModelScope.launch {
                            _user.emit(Resource.Success(user))
                        }
                    }
                }
            }
    }


    fun logout(){
        auth.signOut()

    }

    fun updateUser(user: User, imageUri: Uri?) {
        TODO("Not yet implemented")
    }
}