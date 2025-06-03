package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CrearCuentaViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _idCreado = MutableStateFlow<String?>(null)
    val idCreado: StateFlow<String?> = _idCreado.asStateFlow()

    private val _emailVerificado = MutableStateFlow(false)
    val emailVerificado: StateFlow<Boolean> = _emailVerificado.asStateFlow()

    fun crearUsuario(usuario: Usuario) {
        _loading.value = true
        _error.value = null

        auth.createUserWithEmailAndPassword(usuario.correo, usuario.contrasena)
            .addOnCompleteListener { task ->
                _loading.value = false
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { sendTask ->
                        if (sendTask.isSuccessful) {
                            _idCreado.value = user.uid
                            // Esperamos a que el usuario confirme el correo
                        } else {
                            _error.value = "Error enviando correo de verificación"
                        }
                    }
                } else {
                    _error.value = task.exception?.message ?: "Error creando usuario"
                }
            }
    }

    fun checkEmailVerified() {
        val user: FirebaseUser? = auth.currentUser
        user?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _emailVerificado.value = user.isEmailVerified
            } else {
                _error.value = "Error verificando el estado del correo"
            }
        }
    }
}

