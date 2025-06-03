package es.tierno.mohamed.aa.mohabeatsiii.data.network.open_ai

import es.tierno.mohamed.aa.mohabeatsiii.data.model.chat_bot_model.ChatRespuestaModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.ChatPeticion
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DeepSeekAiApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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"
    )
    @POST("api/v1/chat/completions")
    suspend fun getRespuesta(
        @Body request: ChatPeticion
    ): Response<ChatRespuestaModel>
}