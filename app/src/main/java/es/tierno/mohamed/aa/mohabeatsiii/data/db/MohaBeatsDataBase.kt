package es.tierno.mohamed.aa.mohabeatsiii.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.CancionDescargadaEntidad
import es.tierno.mohamed.aa.mohabeatsiii.data.db.dao.CancionesDescargasDAO

@Database(
    entities = [ CancionDescargadaEntidad::class],
    version = 4,
    exportSchema = false
)
abstract class MohaBeatsDataBase : RoomDatabase() {
    abstract fun cancionDescargadaDao(): CancionesDescargasDAO
}