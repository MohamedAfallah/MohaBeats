package es.tierno.mohamed.aa.mohabeatsiii.domain.model

data class Artista(
    val idArtista: Long,
    val nombreArtista: String,
    val urlArtista: String?,
    val generoPrincipal: String?,
    val tipoArtista: String?,
)