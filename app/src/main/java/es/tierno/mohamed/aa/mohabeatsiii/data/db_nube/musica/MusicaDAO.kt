package es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.musica

import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario

interface MusicaDAO {
    suspend fun obtenerIdCanciones(id : String): List<String>?
    suspend fun insertarAFavoritos(id: String, id_cancion : String)
    suspend fun eliminarFavorito(id: String, id_cancion: String)
}