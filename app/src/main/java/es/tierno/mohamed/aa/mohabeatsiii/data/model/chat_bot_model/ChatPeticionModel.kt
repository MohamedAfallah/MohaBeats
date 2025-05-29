package es.tierno.mohamed.aa.mohabeatsiii.data.model.chat_bot_model

data class ChatPeticionModel(
    val model: String = "deepseek-chat",
    val messages: List<MensajesModel>
)