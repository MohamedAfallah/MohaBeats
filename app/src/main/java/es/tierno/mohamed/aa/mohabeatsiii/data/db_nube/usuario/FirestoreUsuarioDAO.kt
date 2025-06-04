package es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.dao

import android.util.Log
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
            doc.toObject(Usuario::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreUsuarioDAO", "EXCEPCIÓN al obtener usuario con ID '$id': ${e.message}", e)
            null
        }
    }

    override suspend fun crearUsuario(usuario: Usuario): String {
        return try {
            val userData = hashMapOf(
                "nombreCompleto" to usuario.nombreCompleto,
                "fechaNacimiento" to usuario.fechaNacimiento,
                "correo" to usuario.correo,
                "usuario" to usuario.usuario,
                "telefono" to usuario.telefono,
                "contrasena" to usuario.contrasena
            )
            usuariosCollection.document(usuario.id).set(userData).await()
            usuario.id
        } catch (e: Exception) {
            Log.e("FirestoreUsuarioDAO", "Error al crear usuario con ID ${usuario.id}: ${e.message}", e)
            ""
        }
    }

    override suspend fun modificarUsuario(usuario: Usuario) {
        try {
            val updates = hashMapOf<String, Any>(
                "nombreCompleto" to usuario.nombreCompleto,
                "fechaNacimiento" to usuario.fechaNacimiento,
                "usuario" to usuario.usuario,
                "telefono" to usuario.telefono
            )
            usuariosCollection.document(usuario.id).update(updates).await()
        } catch (e: Exception) {
            Log.e("FirestoreUsuarioDAO", "Error al modificar usuario con ID ${usuario.id} en Firestore: ${e.message}", e)
        }
    }
}