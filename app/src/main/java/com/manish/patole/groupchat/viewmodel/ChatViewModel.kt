package com.manish.patole.groupchat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.manish.patole.groupchat.base.BaseViewModel
import com.manish.patole.groupchat.data.ChatMessage
import com.manish.patole.groupchat.util.Constant

class ChatViewModel : BaseViewModel(), ChildEventListener {
    private val databaseReference =
        FirebaseDatabase.getInstance().reference.child(Constant.MESSAGE)
    private var key: String = ""
    private val _receivedChatMessage = MutableLiveData<ChatMessage>()
    val receivedChatMessage: LiveData<ChatMessage>
        get() = _receivedChatMessage

    fun loadMessages() = databaseReference.addChildEventListener(this)

    fun unLoadMessages() = databaseReference.removeEventListener(this)

    fun pushMessage(message: String) = databaseReference.push().setValue(
        ChatMessage(
            currentUser?.displayName ?: Constant.ANONYMOUS, message
        )
    )

    override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
        val chat = dataSnapshot.getValue(ChatMessage().javaClass)
        if (key != dataSnapshot.key) {
            _receivedChatMessage.value = chat
        }
        key = dataSnapshot.key.toString()
    }

    override fun onCancelled(p0: DatabaseError) {
    }

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
    }

    override fun onChildRemoved(p0: DataSnapshot) {
    }
}

