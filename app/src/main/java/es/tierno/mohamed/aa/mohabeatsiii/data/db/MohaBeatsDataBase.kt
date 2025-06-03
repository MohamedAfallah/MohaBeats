package es.tierno.mohamed.aa.mohabeatsiii.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import es.tierno.mohamed.aa.mohabeatsiii.data.db.dao.UsuarioDao
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.UsuarioEntity

@Database(entities = [UsuarioEntity::class, ], version = 1, exportSchema = false)
abstract class MohaBeatsDataBase() : RoomDatabase(){

}