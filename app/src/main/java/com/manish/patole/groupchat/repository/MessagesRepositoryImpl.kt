package com.manish.patole.groupchat.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.manish.patole.groupchat.data.Chat
import com.manish.patole.groupchat.util.Constant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
class MessagesRepositoryImpl : MessageRepository, ChildEventListener {

    private val databaseReference =
        FirebaseDatabase.getInstance().reference.child(Constant.MESSAGE)
    private var key = ""
    private val _messageFlow = MutableStateFlow(Chat())
    private val messageFlow: StateFlow<Chat>
        get() = _messageFlow


    override fun loadMessages() {
        databaseReference.addChildEventListener(this)
    }

    override fun unLoadMessages() {
        databaseReference.removeEventListener(this)
    }

    override fun pushMessage(message: String) {
        databaseReference.push().setValue(
            Chat(FirebaseAuth.getInstance().currentUser?.displayName ?: Constant.ANONYMOUS, message)
        )
    }

    override fun observerIncomingMessages() = messageFlow

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        val chat = snapshot.getValue(Chat().javaClass)
        chat?.let {
            if (key != snapshot.key) {
                _messageFlow.value = it
            }
            key = snapshot.key.toString()
        }
    }

    override fun onCancelled(error: DatabaseError) {
        //No Implementation
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        //No Implementation
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        //No Implementation
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        //No Implementation
    }


}