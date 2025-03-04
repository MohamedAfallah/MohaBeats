package es.tierno.mohamed.aa.mohabeatsiii.data.network

import es.tierno.mohamed.aa.mohabeatsiii.core.RetrofitCreator
import es.tierno.mohamed.aa.mohabeatsiii.data.model.TiempoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TiempoServicio {
    private val retrofit = RetrofitCreator.getRetrofit()

    suspend fun obtenerTiempo(): TiempoModel? {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(TiempoApi::class.java).getTiempo()

            if (response.isSuccessful) {
                val body = response.body()
                body
            } else {
                null
            }
        }
    }
}
