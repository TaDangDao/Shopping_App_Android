package com.example.shoppingapp.firebase

import com.example.shoppingapp.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    auth: FirebaseAuth
    ) {
    private val cartCollection=firestore.collection("users").document(auth.uid!!).collection("cart")

    fun addProduct(cartProduct: CartProduct,onResult:(CartProduct?,Exception?)->Unit){
        cartCollection.document().set(cartProduct).addOnSuccessListener {
            onResult(cartProduct,null)
        }.addOnFailureListener {
            onResult(null,it)
        }


    }
    fun deleteCart(){
        cartCollection.get().addOnCompleteListener {
            task->
            if(task.isSuccessful){
                for(document in task.result!!.documents){
                    document.reference.delete()
                }
            }
        }
    }


    fun increaseQuantity(documentId:String,onResult:(String?,Exception?)->Unit){
        firestore.runTransaction { transition ->
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef, newProductObject)
            }
        }.addOnFailureListener {
            onResult(null,it)
        }.addOnSuccessListener {
            onResult(documentId,null)
        }
    }
    fun decreaseQuantity(documentId:String,onResult:(String?,Exception?)->Unit){
        firestore.runTransaction { transition ->
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity - 1
                if(newQuantity<=0){
                    transition.delete(documentRef)
                }
                else{
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef, newProductObject)}
            }
        }.addOnFailureListener {
            onResult(null,it)
        }.addOnSuccessListener {
            onResult(documentId,null)
        }
    }

}