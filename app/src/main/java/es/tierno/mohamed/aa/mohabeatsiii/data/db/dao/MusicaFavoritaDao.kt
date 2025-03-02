package es.tierno.mohamed.aa.mohabeatsiii.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaEntity

@Dao
interface MusicaFavoritaDao {

    @Query("""
        SELECT musica.* FROM musica 
        INNER JOIN musicaFavorita ON musica.id = musicaFavorita.musicaId
        WHERE musicaFavorita.usuarioId = :usuarioId
    """)
    suspend fun obtenerMusicaFavoritaPorUsuario(usuarioId: Int): List<MusicaEntity>
}