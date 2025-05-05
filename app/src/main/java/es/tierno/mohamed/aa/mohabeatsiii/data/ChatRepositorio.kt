package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.network.OpenAiServicio

//Repositorio para la devolucion del tiempo que hace
class ChatRepositorio {
    private val api = OpenAiServicio()

    suspend fun getRespuesta(mensajeUsuario: String): String? {
        return api.consultar(mensajeUsuario)
    }
}