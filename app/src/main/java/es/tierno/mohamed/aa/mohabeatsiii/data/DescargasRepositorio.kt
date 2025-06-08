package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.db.dao.CancionesDescargasDAO
import es.tierno.mohamed.aa.mohabeatsiii.data.mapper.toCancionDescargadaEntidad
import es.tierno.mohamed.aa.mohabeatsiii.data.mapper.toMusica
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DescargasRepositorio @Inject constructor(
    private val descargasDAO: CancionesDescargasDAO
){
    suspend fun descargarCancion(cancion : Musica){
        descargasDAO.insertarCancionDescargada(cancion.toCancionDescargadaEntidad())
    }

    suspend fun eliminarCancion(cancionId : String){

        descargasDAO.eliminarCancionDescargada(cancionId)
    }

    suspend fun getTodasLasCancionesDescargadas() : List<Musica>{
        return descargasDAO.getTodasLasCancionesDescargadas().first().map { it.toMusica() }
    }
}