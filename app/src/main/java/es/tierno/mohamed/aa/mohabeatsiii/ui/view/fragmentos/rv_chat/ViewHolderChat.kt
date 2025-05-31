package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_chat

import androidx.recyclerview.widget.RecyclerView
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ItemChatMessageBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.Mensaje

class ViewHolderChat(private val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(message: Mensaje) {
        binding.chatMessage.text = message.content

        if (message.role == "user") {
            binding.chatMessage.setBackgroundResource(R.drawable.bg_user_message)
            binding.chatMessage.setTextColor(binding.root.context.getColor(R.color.blancoAgradables))
        } else {
            binding.chatMessage.setBackgroundResource(R.drawable.bg_bot_message)
            binding.chatMessage.setTextColor(binding.root.context.getColor(R.color.grisSuave))
        }
    }
}