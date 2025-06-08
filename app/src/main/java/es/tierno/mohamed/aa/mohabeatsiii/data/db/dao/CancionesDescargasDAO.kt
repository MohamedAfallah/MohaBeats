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
    suspend fun insertarCancionDescargada(cancion: CancionDescargadaEntidad)

    @Query("SELECT * FROM canciones_descargadas ORDER BY nombreCancion ASC")
    fun getTodasLasCancionesDescargadas(): Flow<List<CancionDescargadaEntidad>>

    @Query("DELETE FROM canciones_descargadas WHERE idCancion = :cancionId")
    suspend fun eliminarCancionDescargada(cancionId: String)
}