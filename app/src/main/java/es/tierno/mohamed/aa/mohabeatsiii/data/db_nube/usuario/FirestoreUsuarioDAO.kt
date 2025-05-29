package es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.dao

import com.google.firebase.firestore.FirebaseFirestore
import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.usuario.UsuarioDAO
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUsuarioDAO @Inject constructor(
    private val firestore: FirebaseFirestore
) : UsuarioDAO {

    private val usuariosCollection = firestore.collection("usuarios")

    override suspend fun obtenerUsuario(id: String): Usuario? {
        return try {
            val doc = usuariosCollection.document(id).get().await()
            if (doc.exists()) doc.toObject(Usuario::class.java) else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun crearUsuario(usuario: Usuario): String {
        return try {
            usuariosCollection.document(usuario.id).set(usuario).await()
            usuario.id // devuelves el mismo id que usaste para guardar
        } catch (e: Exception) {
            ""
        }
    }
}