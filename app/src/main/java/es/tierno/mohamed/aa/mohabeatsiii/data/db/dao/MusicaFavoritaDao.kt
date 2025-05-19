package es.tierno.mohamed.aa.mohabeatsiii.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaEntity
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaFavoritaEntity

@Dao
interface MusicaFavoritaDao {

    // Un select realizazdo para que nos devuelva la lista de canciones favoritas de un usuario x
    @Query("""
        SELECT musica.* FROM musica 
        INNER JOIN musicaFavorita ON musica.id = musicaFavorita.musicaId
        WHERE musicaFavorita.usuarioId = :usuarioId
    """)
    suspend fun obtenerMusicaFavoritaPorUsuario(usuarioId: Int): List<MusicaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertarFavoritos(listaFavoritos : List<MusicaFavoritaEntity>)
}