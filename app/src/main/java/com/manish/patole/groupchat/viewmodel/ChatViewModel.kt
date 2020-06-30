package com.manish.patole.groupchat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.manish.patole.groupchat.data.ChatMessage
import com.manish.patole.groupchat.repository.MessageRepository
import com.manish.patole.groupchat.repository.factory.MessageRepositoryFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class ChatViewModel : ViewModel() {
    private val messageRepo: MessageRepository = MessageRepositoryFactory.getMessageRepository()

    private val _receivedChatMessage = messageRepo.observerIncomingMessages().filter { chat ->
        chat.text?.isNotBlank() == true
    }.map { chat ->
        ChatMessage(chat.name, chat.text, chat.photoUrl, true)
    }.asLiveData()

    val receivedChatMessage: LiveData<ChatMessage>
        get() = _receivedChatMessage

    fun loadMessages() = messageRepo.loadMessages()

    fun unLoadMessages() = messageRepo.unLoadMessages()

    fun pushMessage(message: String) = messageRepo.pushMessage(message)
}

