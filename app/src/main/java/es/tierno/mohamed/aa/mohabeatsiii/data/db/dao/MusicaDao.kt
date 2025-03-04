package es.tierno.mohamed.aa.mohabeatsiii.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaEntity

@Dao
interface MusicaDao {
    @Query("SELECT * FROM musica")
    suspend fun obtenerMusica():List<MusicaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMusica(musica: List<MusicaEntity>)
}