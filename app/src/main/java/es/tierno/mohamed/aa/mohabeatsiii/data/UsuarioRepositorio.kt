package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.mapper.toUsuario
import es.tierno.mohamed.aa.mohabeatsiii.data.db.dao.UsuarioDao
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.UsuarioEntity
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import javax.inject.Inject

class UsuarioRepositorio@Inject constructor (
    private val usuarioDao: UsuarioDao) {
    suspend fun getUsuarios(): List<Usuario>{
        val response = usuarioDao.obtenerUsuarios()
        return response.map { it.toUsuario() }
    }

    suspend fun insertarUsuarios(usuarios: List<Usuario>) {
        val usuarioEntities = usuarios.map {
            UsuarioEntity(
                nombre = it.nombre,
                usuario = it.usuario,
                contrasena = it.contrasena
            )
        }
        usuarioDao.insertarUsuarios(usuarioEntities)
    }
}