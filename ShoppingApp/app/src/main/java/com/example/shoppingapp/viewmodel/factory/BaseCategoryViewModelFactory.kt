package com.example.shoppingapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppingapp.data.Category
import com.example.shoppingapp.viewmodel.BaseShoppingViewModel
import com.google.firebase.firestore.FirebaseFirestore

class BaseCategoryViewModelFactory (
    private val firestore: FirebaseFirestore,
    private val category: Category
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BaseShoppingViewModel(firestore,category) as T
    }
}
