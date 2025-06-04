package es.tierno.mohamed.aa.mohabeatsiii.core

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import android.util.Log

class FbAuth {

    private val auth = FirebaseAuth.getInstance()

    suspend fun crearUsuario(email: String, password: String): String {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            firebaseUser?.sendEmailVerification()?.await()

            firebaseUser?.uid ?: ""
        } catch (e: Exception) {
            Log.e("FbAuth", "Error al crear usuario en Firebase Auth: ${e.message}", e)
            ""
        }
    }

    suspend fun updateUserEmail(newEmail: String): Boolean {
        val user = auth.currentUser
        return if (user != null) {
            try {
                user.updateEmail(newEmail).await()
                true
            } catch (e: Exception) {
                Log.e("FbAuth", "Error al actualizar el correo electrónico: ${e.message}", e)
                false
            }
        } else {
            false
        }
    }

    suspend fun updateUserPassword(newPassword: String): Boolean {
        val user = auth.currentUser
        return if (user != null) {
            try {
                user.updatePassword(newPassword).await()
                true
            } catch (e: Exception) {
                Log.e("FbAuth", "Error al actualizar la contraseña: ${e.message}", e)
                false
            }
        } else {
            false
        }
    }
}