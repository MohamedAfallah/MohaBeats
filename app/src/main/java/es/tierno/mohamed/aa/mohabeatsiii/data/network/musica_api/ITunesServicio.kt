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
        Log.d("ITunesServicio", "Iniciando búsqueda con query: '$query'")
        val response = api.buscar(query, limit = 50) // límite aumentado para más resultados
        Log.d("ITunesServicio", "Respuesta recibida, éxito: ${response.isSuccessful}, código: ${response.code()}")

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Log.d("ITunesServicio", "Número de resultados recibidos: ${body.resultados.size}")
                return body.resultados
            } else {
                Log.d("ITunesServicio", "El body de la respuesta es null")
            }
        } else {
            Log.d("ITunesServicio", "Respuesta no exitosa. Código HTTP: ${response.code()}")
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

    suspend fun getCancion(id_cancion : String):Musica?{
        val response = api.getCancion(id_cancion)

        if(response.isSuccessful){
            val body = response.body()
            if(body != null){
                return body.resultados.get(0)
            }
        }

        return null
    }
}
