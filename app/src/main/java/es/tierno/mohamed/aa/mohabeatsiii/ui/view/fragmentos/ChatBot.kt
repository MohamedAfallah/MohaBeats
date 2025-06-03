package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentChatBotBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.Mensaje
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_chat.AdapterChat
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.OpenAiViewModel

@AndroidEntryPoint
class ChatBot : Fragment() {

    private var _binding: FragmentChatBotBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OpenAiViewModel
        get() = ViewModelProvider(this).get(OpenAiViewModel::class.java)

    private val messages = mutableListOf<Mensaje>()
    private lateinit var adapterChat: AdapterChat
    private lateinit var lottieAnimation: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterChat = AdapterChat(messages)
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMessages.adapter = adapterChat

        lottieAnimation = binding.lottieAnimation
        lottieAnimation.visibility = View.GONE

        // Observa las respuestas del ViewModel para actualizar UI
        viewModel.respuesta.observe(viewLifecycleOwner, Observer { respuestas ->
            // Ocultar animación cuando llegan las respuestas
            lottieAnimation.visibility = View.GONE
            lottieAnimation.pauseAnimation()

            respuestas?.let {
                for (mensaje in it) {
                    messages.add(mensaje)
                    adapterChat.notifyItemInserted(messages.size - 1)
                }
                binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
            }
        })

        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                // Agregar mensaje del usuario
                val newMessage = Mensaje(role = "user", content = messageText)
                messages.add(newMessage)
                adapterChat.notifyItemInserted(messages.size - 1)
                binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
                binding.editTextMessage.text.clear()

                // Mostrar animación mientras espera respuesta
                lottieAnimation.visibility = View.VISIBLE
                lottieAnimation.repeatCount = LottieDrawable.INFINITE
                lottieAnimation.playAnimation()

                // Enviar mensaje al ViewModel para obtener respuesta
                viewModel.onCreate(messageText)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}