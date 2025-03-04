package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.model.TiempoModel
import es.tierno.mohamed.aa.mohabeatsiii.data.network.TiempoServicio

//Repositorio para la devolucion del tiempo que hace
class TiempoRepositorio {
    private val api = TiempoServicio()

    suspend fun getTiempo(): TiempoModel? {
        val response = api.obtenerTiempo()

        return response
    }
}