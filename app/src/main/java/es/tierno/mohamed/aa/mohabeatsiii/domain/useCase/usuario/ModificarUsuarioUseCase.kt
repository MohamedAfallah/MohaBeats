package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.usuario

import es.tierno.mohamed.aa.mohabeatsiii.core.FbAuth
import es.tierno.mohamed.aa.mohabeatsiii.data.UsuarioRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import javax.inject.Inject

class ModificarUsuarioUseCase @Inject constructor(
    private val usuarioRepositorio: UsuarioRepositorio
) {
    val fbAuth = FbAuth()

    suspend operator fun invoke(
        userId: String,
        newEmail: String,
        oldEmail: String,
        newPassword: String?,
        newNombreCompleto: String,
        newFechaNacimiento: String,
        newUsuarioDisplayName: String,
        newTelefono: String
    ): Result<Unit> {
        try {
            if (newEmail != oldEmail) {
                val emailUpdated = fbAuth.updateUserEmail(newEmail)
                if (!emailUpdated) {
                    return Result.failure(Exception("Error al actualizar correo en autenticación."))
                }
            }

            if (!newPassword.isNullOrBlank()) {
                val passwordUpdated = fbAuth.updateUserPassword(newPassword)
                if (!passwordUpdated) {
                    return Result.failure(Exception("Error al actualizar contraseña en autenticación."))
                }
            }

            val updatedUserForFirestore = Usuario(
                id = userId,
                nombreCompleto = newNombreCompleto,
                fechaNacimiento = newFechaNacimiento,
                correo = newEmail,
                usuario = newUsuarioDisplayName,
                telefono = newTelefono,
                contrasena = ""
            )

            usuarioRepositorio.modificarUsuario(updatedUserForFirestore)

            return Result.success(Unit)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}