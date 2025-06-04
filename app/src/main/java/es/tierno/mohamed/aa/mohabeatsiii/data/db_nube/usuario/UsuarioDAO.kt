package es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.usuario

import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario

interface UsuarioDAO {
    suspend fun obtenerUsuario(id : String): Usuario?
    suspend fun crearUsuario(usuario : Usuario) : String
    suspend fun  modificarUsuario(usuario : Usuario)
}