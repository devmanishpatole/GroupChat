package com.manish.patole.groupchat.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.manish.patole.groupchat.base.BaseViewModel

class AuthenticationViewModel : BaseViewModel() {
    private var fireBaseAuthListener: FirebaseAuth.AuthStateListener
    private val mFirebaseAuth = FirebaseAuth.getInstance()
    val userAuthentication = MutableLiveData<Boolean>()

    init {
        fireBaseAuthListener = FirebaseAuth.AuthStateListener { auth ->
            userAuthentication.postValue(null != auth.currentUser)
        }
    }

    /**
     * Invokes when activity is resumed
     */
    fun listenForAuthentication() {
        mFirebaseAuth.addAuthStateListener(fireBaseAuthListener)
    }

    /**
     * Invokes when activity is stopped
     */
    fun stopListeningToAuthentication() {
        mFirebaseAuth.removeAuthStateListener(fireBaseAuthListener)
    }
}