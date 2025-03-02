package es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "musica")
data class MusicaEntity (@PrimaryKey(autoGenerate = true)
                        @ColumnInfo(name = "id") val id: Int = 0,
                        @ColumnInfo(name = "nombre") val nombre:String,
                        @ColumnInfo(name = "artidita") val artista:String,
                        @ColumnInfo(name = "url") val url:String )