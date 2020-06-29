package com.manish.patole.groupchat.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
        holder.apply {
            if (!userName.isNullOrEmpty() && userName == chat.name) {
                tvMessage.background = ContextCompat.getDrawable(
                    parent.context, R.drawable.logged_in_round_corner
                )
                parent.gravity = Gravity.END

            } else {
                tvMessage.background = ContextCompat.getDrawable(
                    parent.context, R.drawable.logged_out_round_corner
                )
                parent.gravity = Gravity.START
            }
            tvMessage.text = chat.text

            tvUserName.apply {
                if (chat.shouldShowUserName) {
                    text = chat.name
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }
        }
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
        if (messageList.isNotEmpty() && messageList[messageList.size - 1].name == chat.name) {
            chat.shouldShowUserName = false
        }
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
        val parent: LinearLayout = view.parentView
    }
}