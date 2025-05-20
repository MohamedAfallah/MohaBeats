package es.tierno.mohamed.aa.mohabeatsiii.data.network.open_ai

import es.tierno.mohamed.aa.mohabeatsiii.data.model.chat_bot_model.ChatPeticionModel
import es.tierno.mohamed.aa.mohabeatsiii.data.model.chat_bot_model.ChatRespuestaModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.ChatPeticion
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.ChatRespuesta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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"
    )
    @POST("v1/chat/completions")
    suspend fun getRespuesta(
        @Body request: ChatPeticion
    ): Response<ChatRespuesta>
}