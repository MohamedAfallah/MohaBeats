package es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.posts

import com.google.firebase.firestore.FirebaseFirestore
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Post
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class ProviderPostDAO @Inject constructor(
    private val firebase: FirebaseFirestore
) : PostDAO {

    private fun getPostSubcollectionForUser(idUsuario: String) =
        firebase.collection("posts")
            .document(idUsuario)
            .collection("PostColeccion")

    override suspend fun getPost(): List<Post> {
        val snapshot = firebase.collectionGroup("PostColeccion").get().await()
        return snapshot.documents.mapNotNull { doc ->
            val post = doc.toObject(Post::class.java)
            post?.copy(id = doc.id)
        }
    }

    override suspend fun getPostPorUsuario(idUsuario: String): List<Post> {
        val snapshot = getPostSubcollectionForUser(idUsuario).get().await()
        return snapshot.documents.mapNotNull { doc ->
            val post = doc.toObject(Post::class.java)
            post?.copy(id = doc.id)
        }
    }

    override suspend fun crearPost(
        idUsuario: String,
        comentario: String,
        playlistId: String
    ) {
        val postId = UUID.randomUUID().toString()
        val nuevoPost = Post(
            id = postId,
            idUsuario = idUsuario,
            comentario = comentario,
            playlist = playlistId
        )
        getPostSubcollectionForUser(idUsuario)
            .document(postId)
            .set(nuevoPost)
            .await()
    }

    override suspend fun eliminarPost(idUsuario: String, idPost: String) {
        getPostSubcollectionForUser(idUsuario)
            .document(idPost)
            .delete()
            .await()
    }
}