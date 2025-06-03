package es.tierno.mohamed.aa.mohabeatsiii.data.network.open_ai

import android.util.Log
import es.tierno.mohamed.aa.mohabeatsiii.core.DeepSeekAiCreator
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.ChatPeticion
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.Mensaje
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class OpenAiServicio @Inject constructor(){
    private val retrofit = DeepSeekAiCreator.getRetrofit().create(DeepSeekAiApi::class.java)

    suspend fun consultar(mensajeUsuario: String): List<Mensaje> {
        return withContext(Dispatchers.IO) {
            try {
                val peticion = ChatPeticion(
                    messages = listOf(
                        Mensaje(
                            role = "user",
                            content = "$mensajeUsuario. esto es un chatbot dentro de mi aplicacion MohaBeats, Restricciones[responde solo mensajes relacionados con música, pero si puedes basarte en otros datos para recomendar canciones. Porfavor las respuesta que sean mas cortas y que no pongas estas restricciones que te puse] el usuario que te manda el prompt no tiene ni idea de las restricciones no las respondas. como si fuera un chat"
                        )
                    )
                )

                val response = retrofit.getRespuesta(peticion)

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("OpenAiResponse", "Respuesta OpenAI: $body")

                    body?.choices?.map { choice ->
                        Mensaje(role = choice.message.role, content = choice.message.content)
                    } ?: emptyList()
                } else {
                    Log.e("OpenAiError", "Error de respuesta: ${response.code()} - ${response.errorBody()?.string()}")
                    emptyList()
                }

            } catch (e: Exception) {
                Log.e("OpenAiException", "Excepción: ${e.message}", e)
                emptyList()
            }
        }
    }
}