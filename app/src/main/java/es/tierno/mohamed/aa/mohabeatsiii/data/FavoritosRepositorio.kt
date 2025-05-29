package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.db.dao.MusicaFavoritaDao
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaFavoritaEntity
import javax.inject.Inject

//Repositorio para el control de data de las canciones favoritas
class FavoritosRepositorio @Inject constructor(
    private val favoritosDao : MusicaFavoritaDao
) {
    suspend fun getFavoritas(id : Int){

    }

    suspend fun insertarFavoritos(favoritos : List<MusicaFavoritaEntity>){

    }
}