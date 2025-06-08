package es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.posts

import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Post

interface PostDAO {
    suspend fun getPost(): List<Post>
    suspend fun getPostPorUsuario(idUsuario: String): List<Post>
    suspend fun crearPost(idUsuario: String, comentario: String, playlistId: String)
    suspend fun eliminarPost(idUsuario: String, idPost: String)
}
