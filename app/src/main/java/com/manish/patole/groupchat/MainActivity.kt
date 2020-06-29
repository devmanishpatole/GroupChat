package com.manish.patole.groupchat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.manish.patole.groupchat.adapter.ChatAdapter
import com.manish.patole.groupchat.util.Constant
import com.manish.patole.groupchat.viewmodel.AuthenticationViewModel
import com.manish.patole.groupchat.viewmodel.ChatViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var authModel: AuthenticationViewModel
    private lateinit var chatModel: ChatViewModel
    private var chatAdapter = ChatAdapter(null)

    companion object {
        private const val RC_SIGN_IN = 1
        private const val SCROLL_DELAY = 100L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewModelProvider(this).apply {
            authModel = get(AuthenticationViewModel::class.java)
            chatModel = get(ChatViewModel::class.java)
        }
        handleEditText()
        addObservers()
    }

    private fun addObservers() {
        authModel.userAuthentication.observe(this, Observer { authenticated ->
            if (authenticated) {
                chatAdapter = ChatAdapter(FirebaseAuth.getInstance().currentUser?.displayName)
                chatList.adapter = chatAdapter
                chatModel.loadMessages()
            } else {
                authenticateUser()
            }
        })

        chatModel.receivedChatMessage.observe(this, Observer { chatMessage ->
            progressBar.visibility = View.GONE
            chatAdapter.addMessage(chatMessage)
            chatList.postDelayed({
                chatList.smoothScrollToPosition(chatAdapter.itemCount)
            }, SCROLL_DELAY)

        })

        sendButton.setOnClickListener {
            hideKeyboard()
            chatModel.pushMessage(messageEditText.text.toString())
            messageEditText.setText("")
        }
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

    /**
     * Sets the edit text parameters
     */
    private fun handleEditText() {
        messageEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //No Implementation
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //No Implementation
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendButton.isEnabled = s.toString().isNotEmpty()
            }
        })

        messageEditText.filters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(
                Constant.MAX_TEXT_LENGTH
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.sign_out) {
            chatModel.unLoadMessages()
            chatAdapter.clear()
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

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null && view.windowToken != null) {
            val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
