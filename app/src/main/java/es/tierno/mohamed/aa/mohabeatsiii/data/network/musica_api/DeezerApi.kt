package es.tierno.mohamed.aa.mohabeatsiii.data.network.musica_api

import es.tierno.mohamed.aa.mohabeatsiii.data.model.BusquedaModel
import es.tierno.mohamed.aa.mohabeatsiii.data.model.MusicaModel
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response


interface DeezerApi {
    // Buscar artistas, canciones y álbumes por palabra clave
    @GET("search")
    suspend fun buscar(@Query("q") query: String): Response<BusquedaModel>

    // Obtener una canción aleatoria (usando radio para obtener listas aleatorias)
    @GET("radio/37151/tracks")
    suspend fun getCancionesRandom(): Response<List<MusicaModel>>
}