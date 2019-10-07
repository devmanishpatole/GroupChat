package com.manish.patole.groupchat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.manish.patole.groupchat.R
import com.manish.patole.groupchat.data.ChatMessage
import kotlinx.android.synthetic.main.row_chat.view.*

/**
 * Chat Adapter
 *
 * @author Manish Patole
 */
class ChatAdapter(private val userName: String?) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = messageList[position]
        if (!userName.isNullOrEmpty() && userName == chat.name) {
            holder.tvMessage.background = ContextCompat.getDrawable(
                holder.parent.context, R.drawable.logged_in_round_corner
            )
        } else {
            holder.tvMessage.background = ContextCompat.getDrawable(
                holder.parent.context, R.drawable.logged_out_round_corner
            )
        }
        holder.tvMessage.text = chat.text
        holder.tvUserName.text = chat.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_chat,
                parent, false
            )
        )
    }

    private val messageList: MutableList<ChatMessage> = ArrayList()

    /**
     * Returns the total number of items in the data set held by the adapter.

     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return messageList.size
    }

    /**
     * Appends chat message
     */
    fun addMessage(chat: ChatMessage) {
        messageList.add(chat)
        notifyItemChanged(messageList.lastIndex)
    }

    fun clear() {
        messageList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: TextView = view.userName
        val tvMessage: TextView = view.message
        val parent: View = view.parentView

    }
}