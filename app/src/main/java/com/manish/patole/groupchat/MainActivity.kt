package com.manish.patole.groupchat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.manish.patole.groupchat.viewmodel.AuthenticationViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var authModel: AuthenticationViewModel

    companion object {
        private const val RC_SIGN_IN = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        authModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
        addObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.sign_out) {
            AuthUI.getInstance().signOut(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_CANCELED) finish()
    }

    override fun onResume() {
        super.onResume()
        authModel.listenForAuthentication()
    }

    override fun onStop() {
        super.onStop()
        authModel.stopListeningToAuthentication()
    }

    private fun addObservers() {
        authModel.userAuthentication.observe(this, Observer { authenticated ->
            if (authenticated) {
                //TODO
            } else {
                authenticateUser()
            }
        })
    }

    private fun authenticateUser() =
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                .setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build(),
                        AuthUI.IdpConfig.PhoneBuilder().build()
                    )
                )
                .build(),
            RC_SIGN_IN
        )
}
