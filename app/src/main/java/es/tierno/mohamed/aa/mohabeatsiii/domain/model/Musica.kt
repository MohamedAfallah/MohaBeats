package es.tierno.mohamed.aa.mohabeatsiii.domain.model

data class Musica (
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