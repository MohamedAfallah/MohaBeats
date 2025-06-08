package es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.historial

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProviderHistorialDAO @Inject constructor(
    private val db: FirebaseFirestore
) : HistorialDAO {
    override suspend fun getHistorial(idUsuario: String): List<String>? {
        return try {
            val snapshot = db.collection("historial")
                .document(idUsuario)
                .get()
                .await()

            snapshot.get("id_canciones") as? List<String>
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertarHistorial(idUsuario: String, idCancion: String) {
        try {
            val userRef = db.collection("historial").document(idUsuario)
            val data = mapOf("id_canciones" to FieldValue.arrayUnion(idCancion))
            userRef.set(data, SetOptions.merge()).await()
        } catch (e: Exception) {
        }
    }
}