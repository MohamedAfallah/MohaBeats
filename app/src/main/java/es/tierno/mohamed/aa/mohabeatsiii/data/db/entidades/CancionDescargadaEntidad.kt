package es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "canciones_descargadas"
)
data class CancionDescargadaEntidad(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idCancion: String,
    val nombreCancion: String,
    val idArtista: Long,
    val nombreArtista: String,
    val idAlbum: Long?,
    val nombreAlbum: String?,
    val urlImagen: String,
    val urlPreview: String?,
    val genero: String,
    val duracionMillis: Long,
    val fechaLanzamiento: String,
    val rutaLocalCancion: String? = null,
    val rutaLocalImg: String? = null
)