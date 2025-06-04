package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.network.musica_api.ITunesServicio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import javax.inject.Inject

class MusicaRepositorio @Inject constructor(
    private val iTunesServicio: ITunesServicio
) {

    suspend fun obtenerCanciones(): List<Musica> {
        return iTunesServicio.getCanciones() ?: emptyList()
    }

    suspend fun getCancionesPorGenero(generoId : String): List<Musica> {
        return iTunesServicio.getCancionesPorGenero(generoId) ?: emptyList()
    }

    suspend fun buscar(dato : String) : List<Musica>{
        return iTunesServicio.buscar(dato) ?: emptyList()
    }

    suspend fun obtenerCancion(id : String) : Musica?{
        return iTunesServicio.getCancion(id)
    }
}

