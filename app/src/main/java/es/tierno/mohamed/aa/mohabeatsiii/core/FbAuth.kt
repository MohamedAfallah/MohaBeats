package es.tierno.mohamed.aa.mohabeatsiii.core

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FbAuth {

    private val auth = FirebaseAuth.getInstance()

    suspend fun crearUsuario(email: String, password: String): String {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            // Envía el email de verificación si el usuario no es nulo
            firebaseUser?.sendEmailVerification()?.await()

            // Devuelve el uid o cadena vacía si es nulo
            firebaseUser?.uid ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}