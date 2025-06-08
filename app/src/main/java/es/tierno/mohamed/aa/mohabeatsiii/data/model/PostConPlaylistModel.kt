package es.tierno.mohamed.aa.mohabeatsiii.data.model

import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Playlist
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario

data class PostConPlaylistModel(
    val id: String = "",
    val usuario: Usuario,
    val comentario: String = "",
    val playlist: Playlist
)
