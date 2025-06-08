package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.posts.PostDAO
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Post
import javax.inject.Inject

class PostRepositorio @Inject constructor(
    private val postDAO: PostDAO
) {

    suspend fun getPost(): List<Post> {
        return postDAO.getPost()
    }

    suspend fun getPostPorUsuario(idUsuario: String): List<Post> {
        return postDAO.getPostPorUsuario(idUsuario)
    }

    suspend fun crearPost(idUsuario: String,  comentario: String, playlistId: String) {
        postDAO.crearPost(idUsuario,  comentario, playlistId)
    }

    suspend fun eliminarPost(idUsuario: String, idPost: String) {
        return postDAO.eliminarPost(idUsuario, idPost)
    }
}
