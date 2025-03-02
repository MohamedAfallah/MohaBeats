package es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "usuario")
data class UsuarioEntity(@PrimaryKey(autoGenerate = true)
                        @ColumnInfo(name = "id") val id : Int = 0,
                        @ColumnInfo(name = "nombre") val nombre:String,
                        @ColumnInfo(name = "usuario") val usuario:String,
                        @ColumnInfo(name = "contrasena") val contrasena:String)