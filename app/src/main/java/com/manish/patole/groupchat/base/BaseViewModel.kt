package com.manish.patole.groupchat.base

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

open class BaseViewModel : ViewModel() {
    val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser
}