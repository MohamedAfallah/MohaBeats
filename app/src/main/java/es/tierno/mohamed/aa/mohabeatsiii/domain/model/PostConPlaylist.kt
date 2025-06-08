package es.tierno.mohamed.aa.mohabeatsiii.domain.model

data class PostConPlaylist(
    val id: String = "",
    val usuario: Usuario,
    val comentario: String = "",
    val playlist: Playlist
)