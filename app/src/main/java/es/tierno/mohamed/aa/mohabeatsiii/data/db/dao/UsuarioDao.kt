package es.tierno.mohamed.aa.mohabeatsiii.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.UsuarioEntity

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario")
    suspend fun obtenerUsuarios():List<UsuarioEntity>
}