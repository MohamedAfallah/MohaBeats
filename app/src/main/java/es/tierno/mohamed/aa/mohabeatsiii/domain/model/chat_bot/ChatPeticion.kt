package es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot

data class ChatPeticion (val model: String = "gpt-3.5-turbo", val messages: List<Mensaje>)
