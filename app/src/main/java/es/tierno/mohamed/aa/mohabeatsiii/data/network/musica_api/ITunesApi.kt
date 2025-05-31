package es.tierno.mohamed.aa.mohabeatsiii.data.network.musica_api

import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Busqueda
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ITunesApi {
    // obtener canciones por nombre, artista o album o cualquier palabra clave.
    @GET("search")
    suspend fun buscar(
        @Query("term") query: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 5
    ): Response<Busqueda>

    // Obtener canciones aleatorias (esto no existe en la API de iTunes, pero puedes usar búsquedas populares)
    @GET("search")
    suspend fun getCanciones(
        @Query("term") query: String = "midas alonso",
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 50
    ): Response<Busqueda>

    @GET("search")
    suspend fun getCancionesPorGenero(
        @Query("term") query: String = "music",
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("genreId") genreId: String,
        @Query("limit") limit: Int = 10
    ): Response<Busqueda>
}
