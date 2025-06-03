package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.musica.ProviderMusicaDAO
import javax.inject.Inject

//Repositorio para el control de data de las canciones favoritas
class FavoritosRepositorio @Inject constructor(
    private val favoritosDao : ProviderMusicaDAO
) {
    suspend fun getFavoritas(id : String) : List<String>?{
        return favoritosDao.obtenerIdCanciones(id)
    }

    suspend fun insertarFavoritos(id: String, id_cancion : String){
        favoritosDao.insertarAFavoritos(id, id_cancion)
    }
}