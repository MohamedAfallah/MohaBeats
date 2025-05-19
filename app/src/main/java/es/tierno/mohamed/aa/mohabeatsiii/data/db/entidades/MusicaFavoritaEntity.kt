package es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "musicaFavorita", primaryKeys = ["usuarioId", "musicaId"])
data class MusicaFavoritaEntity(
                                @ColumnInfo(name = "usuarioId") val usuarioId: Int,
                                @ColumnInfo(name = "musicaId") val musicaId: Int
)
