package com.manish.patole.groupchat.data

/**
 * Represent chat message
 *
 * @author Manish Patole
 */
data class ChatMessage(
    var name: String? = null,
    var text: String? = null,
    var photoUrl: String? = null,
    var shouldShowUserName: Boolean = true
)