package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase

import es.tierno.mohamed.aa.mohabeatsiii.data.ChatRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.Mensaje
import javax.inject.Inject

// caso de uso para obtener la respuesta de la IA.
class GetRespuestaChatUseCase @Inject constructor(
    private val repositorio : ChatRepositorio
){
    suspend operator fun invoke(mensajeUsuario: String): List<Mensaje>? = repositorio.getRespuesta(mensajeUsuario)
}