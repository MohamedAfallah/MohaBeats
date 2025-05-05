package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.network.open_ai.OpenAiServicio
import javax.inject.Inject

//Repositorio para la devolucion del tiempo que hace
class ChatRepositorio @Inject constructor(private val api : OpenAiServicio) {
    suspend fun getRespuesta(mensajeUsuario: String): String? {
        return api.consultar(mensajeUsuario)
    }
}