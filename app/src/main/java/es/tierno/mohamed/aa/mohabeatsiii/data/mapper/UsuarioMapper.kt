package es.tierno.mohamed.aa.mohabeatsiii.data.mapper

import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.UsuarioEntity
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario

fun UsuarioEntity.toUsuario(): Usuario {
    return Usuario(
        nombre = this.nombre,
        usuario = this.usuario,
        contrasena = this.contrasena
    )
}
