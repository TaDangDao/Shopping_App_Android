package com.example.shoppingapp.data

sealed class Category(val category: String) {
    object Home: Category("Home")
    object Chair: Category("chair")
    object Cupboard: Category("Cupboard")
    object Table: Category("Table")
    object Accessory: Category("Accessory")
    object Furniture: Category("Furniture")
}
