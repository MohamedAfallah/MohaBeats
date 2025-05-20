package es.tierno.mohamed.aa.mohabeatsiii.data.network.musica_api

import es.tierno.mohamed.aa.mohabeatsiii.data.model.BusquedaModel
import es.tierno.mohamed.aa.mohabeatsiii.data.model.MusicaModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Busqueda
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ITunesApi {
    // Buscar artistas, canciones y álbumes por palabra clave
    @GET("search")
    suspend fun buscar(
        @Query("term") query: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 10
    ): Response<BusquedaModel>

    // Obtener canciones aleatorias (esto no existe en la API de iTunes, pero puedes usar búsquedas populares)
    @GET("search")
    suspend fun getCancionesRandom(
        @Query("term") query: String = "pop",
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 10
    ): Response<BusquedaModel>
}
