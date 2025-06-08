package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_chat

import androidx.recyclerview.widget.RecyclerView
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ItemChatMessageBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.Mensaje
import io.noties.markwon.Markwon

class ViewHolderChat(private val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(message: Mensaje) {
        val markwon = Markwon.create(binding.root.context)

        markwon.setMarkdown(binding.chatMessage, message.content)

        if (message.role == "user") {
            binding.chatMessage.setBackgroundResource(R.drawable.bg_user_message)
            binding.chatMessage.setTextColor(binding.root.context.getColor(R.color.blancoAgradables))
        } else {
            binding.chatMessage.setBackgroundResource(R.drawable.bg_bot_message)
            binding.chatMessage.setTextColor(binding.root.context.getColor(R.color.grisSuave))
        }
    }
}
