package com.manish.patole.groupchat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel : ViewModel() {
    private var fireBaseAuthListener: FirebaseAuth.AuthStateListener
    private val mFirebaseAuth = FirebaseAuth.getInstance()
    private val _userAuthentication = MutableLiveData<Boolean>()
    val userAuthentication: LiveData<Boolean>
        get() = _userAuthentication

    init {
        fireBaseAuthListener = FirebaseAuth.AuthStateListener { auth ->
            _userAuthentication.postValue(null != auth.currentUser)
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