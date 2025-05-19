package es.tierno.mohamed.aa.mohabeatsiii.data.mapper

import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.UsuarioEntity
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario

//Un mapper para convertir UsuarioEntity en Usuario
fun UsuarioEntity.toUsuario(): Usuario {
    return Usuario(
        id = this.id,
        nombre = this.nombre,
        usuario = this.usuario,
        contrasena = this.contrasena
    )
}
