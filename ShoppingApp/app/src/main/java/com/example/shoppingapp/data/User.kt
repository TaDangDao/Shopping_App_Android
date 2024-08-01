package com.example.shoppingapp.data

import android.provider.ContactsContract.CommonDataKinds.Email

data class User(val fName:String,val lName:String,val email: String,var imgpath:String=""){
    constructor():this("","","")
}
