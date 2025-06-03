package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_chat

import androidx.recyclerview.widget.RecyclerView
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ItemChatMessageBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.Mensaje
import io.noties.markwon.Markwon

class ViewHolderChat(private val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(message: Mensaje) {
        // Crear una instancia de Markwon para el contexto actual
        val markwon = Markwon.create(binding.root.context)

        // Renderizar el contenido Markdown
        markwon.setMarkdown(binding.chatMessage, message.content)

        // Cambiar estilo según el rol
        if (message.role == "user") {
            binding.chatMessage.setBackgroundResource(R.drawable.bg_user_message)
            binding.chatMessage.setTextColor(binding.root.context.getColor(R.color.blancoAgradables))
        } else {
            binding.chatMessage.setBackgroundResource(R.drawable.bg_bot_message)
            binding.chatMessage.setTextColor(binding.root.context.getColor(R.color.grisSuave))
        }
    }
}
