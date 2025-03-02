package es.tierno.mohamed.aa.mohabeatsiii.data.db

import androidx.room.Database
import es.tierno.mohamed.aa.mohabeatsiii.data.db.dao.MusicaDao
import es.tierno.mohamed.aa.mohabeatsiii.data.db.dao.MusicaFavoritaDao
import es.tierno.mohamed.aa.mohabeatsiii.data.db.dao.UsuarioDao
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaEntity
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaFavoritaEntity
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.UsuarioEntity

@Database(entities = [UsuarioEntity::class, MusicaEntity::class, MusicaFavoritaEntity::class], version = 1)
abstract class MohaBeatsDataBase() {
    abstract fun musicaFavoritaDao(): MusicaFavoritaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun musicaDao(): MusicaDao
}