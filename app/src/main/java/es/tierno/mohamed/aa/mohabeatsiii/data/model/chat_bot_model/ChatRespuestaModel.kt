package es.tierno.mohamed.aa.mohabeatsiii.data.model.chat_bot_model

data class ChatRespuestaModel(
    val choices: List<Choice>
)

data class Choice(
    val message: MensajesModel
)