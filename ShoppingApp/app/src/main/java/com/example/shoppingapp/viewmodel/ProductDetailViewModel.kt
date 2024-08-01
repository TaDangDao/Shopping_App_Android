package com.example.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.CartProduct
import com.example.shoppingapp.firebase.FirebaseCommon
import com.example.shoppingapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProductDetailViewModel@Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    val firebaseCommon: FirebaseCommon
) :ViewModel(){
    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart=_addToCart.asStateFlow()

    fun UpdateAdddCartProduct(cartProduct: CartProduct) {
        firestore.collection("users").document(auth.uid!!).collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id).get()
            .addOnSuccessListener {
                val document = it.toObjects(cartProduct::class.java)
                if (document.isNotEmpty()) {
                    val documentId=it.first().id
                    val product = document.first()
                    if (cartProduct.product.id == product.product.id && product.color == cartProduct.color && product.size == cartProduct.size) {
                        increaseProduct(documentId, cartProduct)
                    } else {
                        addCartProduct(cartProduct)
                    }
                }else{
                    addCartProduct(cartProduct)
                }
            }
    }


        fun addCartProduct(cartProduct: CartProduct) {
            viewModelScope.launch {
                _addToCart.emit(Resource.Loading())
            }
            firebaseCommon.addProduct(cartProduct) { addedProduct, e ->
                if (e != null) {
                    viewModelScope.launch {
                        _addToCart.emit(Resource.Error(e?.message.toString()))
                    }
                } else {
                    viewModelScope.launch {

                        _addToCart.emit(Resource.Success(addedProduct!!))
                    }

                }
            }


        }
    fun increaseProduct(documentId:String,cartProduct: CartProduct){
        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
        }
        firebaseCommon.increaseQuantity(documentId){
            _,e->
            if(e!=null){
                viewModelScope.launch {
                    _addToCart.emit(Resource.Error(e.message.toString()))
                }

                }else{
                    viewModelScope.launch {
                        _addToCart.emit(Resource.Success(cartProduct))
                    }
            }
        }
    }
}