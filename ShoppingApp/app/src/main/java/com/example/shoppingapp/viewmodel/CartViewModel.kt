package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.CartProduct
import com.example.shoppingapp.firebase.FirebaseCommon
import com.example.shoppingapp.helper.getProductPrice
import com.example.shoppingapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CartViewModel@Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    val firebaseCommon: FirebaseCommon
): ViewModel() {
    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()
    val productsPrice = cartProducts.map {
        when (it) {
            is Resource.Success -> {
                CalculatePrice(it.data!!)
            }
            else -> null
        }
    }

    private fun CalculatePrice(data: List<CartProduct>):Float {
        return data.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantity).toDouble()
        }.toFloat()
    }

    init {
        getData()
    }
    var documentList= emptyList<DocumentSnapshot>()
    fun getData(){
        viewModelScope.launch{
            _cartProducts.emit(Resource.Loading())
        }
        firestore.collection("users").document(auth.uid!!).collection("cart")
            .addSnapshotListener{value,error->
                if(error!=null||value==null){
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Error(error?.message.toString()))
                    }
                }
                else{
                    documentList=value.documents
                    val cartProducts=value.toObjects(CartProduct::class.java)

                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Success(cartProducts))
                    }

                }
        }
    }
    fun changeQuantity(cartProduct: CartProduct,quantityChanging:String){
        val index=cartProducts.value.data?.indexOf(cartProduct)
        if(index!=null&&index!=-1){
            val documentId=documentList[index].id
            when(quantityChanging){
                "increase"->{
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Loading())
                    }
                  increaseCartProduct(documentId)
                }
                "decrease"->{
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Loading())
                    }
                  decreaseCartProduct(documentId)
                }
                else->Unit

            }
        }

    }

    private fun decreaseCartProduct(documentId: String) {
             firebaseCommon.decreaseQuantity(documentId){
                 _,e->
                 if(e!=null){
                     viewModelScope.launch {
                         _cartProducts.emit(Resource.Error(e.message.toString()))
                     }
                 }
             }
    }

    private fun increaseCartProduct(documentId: String) {
        firebaseCommon.increaseQuantity(documentId){
                _,e->
            if(e!=null){
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }


}