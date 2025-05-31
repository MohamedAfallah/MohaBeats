package es.tierno.mohamed.aa.mohabeatsiii.data.network.musica_api

import android.util.Log
import es.tierno.mohamed.aa.mohabeatsiii.data.model.BusquedaModel
import es.tierno.mohamed.aa.mohabeatsiii.data.model.MusicaModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Busqueda
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import retrofit2.Response
import javax.inject.Inject

class ITunesServicio @Inject constructor(
    private val api: ITunesApi
) {
    suspend fun buscar(query: String): List<Musica>? {
        val response = api.buscar(query)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body.resultados
            }
        }
        return null
    }

    suspend fun getCanciones(): List<Musica>? {
        val response = api.getCanciones()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body.resultados
            }
        }
        return null
    }

    suspend fun getCancionesPorGenero(generoId : String): List<Musica>?{
        val response = api.getCancionesPorGenero(genreId = generoId)

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Log.e("Cancionesss", "Entro")
                return body.resultados
            }
        }

        return null
    }
}
