package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.model.Tiempo
import es.tierno.mohamed.aa.mohabeatsiii.data.network.TiempoServicio

class TiempoRepositorio {
    private val api = TiempoServicio()

    suspend fun getTiempo(): Tiempo? {
        val response = api.obtenerTiempo()

        return response
    }
}