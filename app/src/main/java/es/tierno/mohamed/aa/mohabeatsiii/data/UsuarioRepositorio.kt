package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.usuario.UsuarioDAO
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import javax.inject.Inject

// Repositorio para el control de usuarios desde Firestore
class UsuarioRepositorio @Inject constructor(
    private val usuarioDAO: UsuarioDAO
) {
    suspend fun obtenerUsuario(id: String): Usuario? {
        return usuarioDAO.obtenerUsuario(id)
    }

    suspend fun crearUsuario(usuario: Usuario): String {
        return usuarioDAO.crearUsuario(usuario)
    }

    suspend fun modificarUsuario(usuario : Usuario){
        usuarioDAO.modificarUsuario(usuario)
    }
}