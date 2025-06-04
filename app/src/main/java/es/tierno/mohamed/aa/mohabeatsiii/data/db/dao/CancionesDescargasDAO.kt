package es.tierno.mohamed.aa.mohabeatsiii.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.CancionDescargadaEntidad

@Dao
interface CancionesDescargasDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCancionDescargada(cancion: CancionDescargadaEntidad)

    @Query("SELECT * FROM canciones_descargadas ORDER BY nombreCancion ASC")
    fun getAllCancionesDescargadas(): Flow<List<CancionDescargadaEntidad>>

    @Query("DELETE FROM canciones_descargadas WHERE id = :cancionId")
    suspend fun deleteCancionDescargadaById(cancionId: Long)
}