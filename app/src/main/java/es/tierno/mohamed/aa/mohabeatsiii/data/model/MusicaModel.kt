package es.tierno.mohamed.aa.mohabeatsiii.data.model

data class MusicaModel(
    val idCancion: Long,
    val nombreCancion: String,
    val idArtista: Long,
    val nombreArtista: String,
    val idAlbum: Long?,
    val nombreAlbum: String?,
    val urlImagen: String,
    val urlPreview: String?,
    val genero: String,
    val duracionMillis: Long,
    val fechaLanzamiento: String
)

