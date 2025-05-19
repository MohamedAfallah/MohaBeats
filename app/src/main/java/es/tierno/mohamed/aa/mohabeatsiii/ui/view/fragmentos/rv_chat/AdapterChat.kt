package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ItemChatMessageBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.Mensaje

class AdapterChat(private val messages: List<Mensaje>) : RecyclerView.Adapter<ViewHolderChat>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderChat {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderChat(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderChat, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size
}