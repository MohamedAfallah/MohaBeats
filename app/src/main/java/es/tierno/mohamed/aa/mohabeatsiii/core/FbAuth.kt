package es.tierno.mohamed.aa.mohabeatsiii.core

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FbAuth {

    private val auth = FirebaseAuth.getInstance()

    suspend fun crearUsuario(email: String, password: String): String {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.uid ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}