package es.tierno.mohamed.aa.mohabeatsiii.data.network.musica_api

import es.tierno.mohamed.aa.mohabeatsiii.core.RetrofitCreator
import es.tierno.mohamed.aa.mohabeatsiii.data.model.MusicaModel
import es.tierno.mohamed.aa.mohabeatsiii.data.model.BusquedaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ITunesServicio {
    private val retrofit = RetrofitCreator.getRetrofit()
    private val api = retrofit.create(ITunesApi::class.java)

    suspend fun search(query: String): BusquedaModel? {
        return withContext(Dispatchers.IO) {
            val response = api.buscar(query)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }
    suspend fun getRandomTracks(): List<MusicaModel>? {
        return withContext(Dispatchers.IO) {
            val response = api.getCancionesRandom()
            if (response.isSuccessful) {
                response.body()?.resultados // <-- accedes a la lista
            } else {
                null
            }
        }
    }

}