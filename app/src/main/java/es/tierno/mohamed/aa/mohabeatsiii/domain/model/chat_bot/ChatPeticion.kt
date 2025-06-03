package es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot

data class ChatPeticion (val model: String = "deepseek/deepseek-r1:free", val messages: List<Mensaje>)
