package es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.musica

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProviderMusicaDAO @Inject constructor(
    private val db: FirebaseFirestore
) : MusicaDAO{
    override suspend fun obtenerIdCanciones(id: String): List<String>? {
        return try {
            val snapshot = db.collection("favoritos")
                .document(id)
                .get()
                .await()

            snapshot.get("id_canciones") as? List<String>
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    override suspend fun insertarAFavoritos(id: String, id_cancion: String) {
        try {
            val userRef = db.collection("favoritos").document(id)
            val data = mapOf("id_canciones" to FieldValue.arrayUnion(id_cancion))
            userRef.set(data, SetOptions.merge()).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override suspend fun eliminarFavorito(id: String, id_cancion: String) {
        try {
            val userRef = db.collection("favoritos").document(id)
            val data = mapOf("id_canciones" to FieldValue.arrayRemove(id_cancion))
            userRef.set(data, SetOptions.merge()).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}