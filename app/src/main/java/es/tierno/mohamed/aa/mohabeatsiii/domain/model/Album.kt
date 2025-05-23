package es.tierno.mohamed.aa.mohabeatsiii.domain.model

data class Album(
    val idAlbum: Long,
    val nombreAlbum: String,
    val idArtista: Long,
    val nombreArtista: String,
    val imagenAlbum: String?,
    val fechaLanzamiento: String?,
    val generoPrincipal: String?,
    val numeroCanciones: Int?,
    val copyright: String?,
    val pais: String?
)