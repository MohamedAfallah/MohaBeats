package es.tierno.mohamed.aa.mohabeatsiii.data.model.chat_bot_model

data class ChoiceModel(
    val index: Int,
    val message: MensajesModel,
    val finish_reason: String
)