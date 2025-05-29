package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.usuario

import es.tierno.mohamed.aa.mohabeatsiii.core.FbAuth
import es.tierno.mohamed.aa.mohabeatsiii.data.UsuarioRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import javax.inject.Inject

class CrearUsuarioUseCase @Inject constructor(
    private val repositorio: UsuarioRepositorio,
    private val auth: FbAuth
) {
    suspend operator fun invoke(usuario: Usuario): String {
        val uid = auth.crearUsuario(usuario.correo, usuario.contrasena)
        if (uid.isEmpty()) return ""

        // Crear usuario Firestore con id = uid
        val usuarioConId = usuario.copy(id = uid)
        repositorio.crearUsuario(usuarioConId)

        return uid
    }
}