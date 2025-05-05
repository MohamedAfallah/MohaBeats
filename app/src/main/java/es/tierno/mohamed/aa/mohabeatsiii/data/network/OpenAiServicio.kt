package es.tierno.mohamed.aa.mohabeatsiii.data.network

import es.tierno.mohamed.aa.mohabeatsiii.core.RetrofitCreator
import es.tierno.mohamed.aa.mohabeatsiii.data.model.chat_bot_model.ChatPeticionModel
import es.tierno.mohamed.aa.mohabeatsiii.data.model.chat_bot_model.MensajesModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OpenAiServicio {
    private val retrofit = RetrofitCreator.getRetrofit()

    suspend fun consultar(mensajeUsuario: String): String? {
        return withContext(Dispatchers.IO) {
            val peticion = ChatPeticionModel(
                model = "gpt-3.5-turbo",
                messages = listOf(
                    MensajesModel(role = "user", content = mensajeUsuario)
                )
            )

            val response = retrofit.create(OpenAiApi::class.java).getRespuesta(peticion)

            if (response.isSuccessful) {
                response.body()?.choices?.firstOrNull()?.message?.content
            } else {
                null
            }
        }
    }
}
