package com.manish.patole.groupchat.repository.factory

import com.manish.patole.groupchat.repository.MessagesRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi

object MessageRepositoryFactory {

    @ExperimentalCoroutinesApi
    fun getMessageRepository() = MessagesRepositoryImpl() //For now we only has single implementation for repository

}