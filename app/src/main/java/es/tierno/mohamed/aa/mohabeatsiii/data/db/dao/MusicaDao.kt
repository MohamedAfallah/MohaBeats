package es.tierno.mohamed.aa.mohabeatsiii.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaEntity

@Dao
interface MusicaDao {
    @Query("SELECT * FROM musica")
    suspend fun obtenerMusica():List<MusicaEntity>
}