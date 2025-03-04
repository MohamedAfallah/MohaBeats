package es.tierno.mohamed.aa.mohabeatsiii.data.network

import es.tierno.mohamed.aa.mohabeatsiii.data.model.TiempoModel
import retrofit2.Response
import retrofit2.http.GET


interface TiempoApi {
    @GET("provincias/28")
    suspend fun getTiempo() : Response<TiempoModel>
}