package es.tierno.mohamed.aa.mohabeatsiii.data.network.open_ai

import es.tierno.mohamed.aa.mohabeatsiii.core.RetrofitCreator
import es.tierno.mohamed.aa.mohabeatsiii.data.model.chat_bot_model.ChatPeticionModel
import es.tierno.mohamed.aa.mohabeatsiii.data.model.chat_bot_model.MensajesModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.ChatPeticion
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.Mensaje
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class OpenAiServicio @Inject constructor(){
    private val retrofit = RetrofitCreator.getRetrofit().create(OpenAiApi::class.java)

    suspend fun consultar(mensajeUsuario: String): List<Mensaje> {
        return withContext(Dispatchers.IO) {
            try {
                val peticion = ChatPeticion(messages = listOf(Mensaje(
                    role = "user",
                    mensajes = mensajeUsuario + " Responde solo preguntas relacionadas con la música"
                )))
                val response = retrofit.getRespuesta(peticion)

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        println("Respuesta OpenAI: $body")
                    }
                    response.body()?.choices?.map { choice ->
                        Mensaje(role = choice.message.role, mensajes = choice.message.content)
                    } ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

}