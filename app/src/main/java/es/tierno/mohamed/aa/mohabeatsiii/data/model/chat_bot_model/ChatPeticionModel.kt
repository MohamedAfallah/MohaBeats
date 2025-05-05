package es.tierno.mohamed.aa.mohabeatsiii.data.model.chat_bot_model

data class ChatPeticionModel(
    val model: String = "gpt-3.5-turbo",
    val messages: List<MensajesModel>
)