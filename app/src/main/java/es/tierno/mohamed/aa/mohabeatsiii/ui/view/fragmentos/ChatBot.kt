package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentChatBotBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.Mensaje
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_chat.AdapterChat

class ChatBot : Fragment() {
    private var _binding: FragmentChatBotBinding? = null
    private val binding get() = _binding!!

    private val messages = mutableListOf<Mensaje>()
    private lateinit var adapterChat: AdapterChat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterChat = AdapterChat(messages)
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMessages.adapter = adapterChat

        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val newMessage = Mensaje(role = "user", mensajes = messageText)
                messages.add(newMessage)
                adapterChat.notifyItemInserted(messages.size - 1)
                binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
                binding.editTextMessage.text.clear()

                // Aquí podrías llamar a la API o agregar la respuesta del bot
                // Por ejemplo, simular respuesta del bot:
                simulateBotResponse("Recibí: $messageText")
            }
        }
    }

    private fun simulateBotResponse(responseText: String) {
        val botMessage = Mensaje(role = "bot", mensajes = responseText)
        messages.add(botMessage)
        adapterChat.notifyItemInserted(messages.size - 1)
        binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
