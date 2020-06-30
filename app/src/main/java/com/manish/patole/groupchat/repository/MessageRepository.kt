package com.manish.patole.groupchat.repository

import com.manish.patole.groupchat.data.Chat
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun loadMessages()

    fun unLoadMessages()

    fun pushMessage(message: String)

    fun observerIncomingMessages(): Flow<Chat>
}