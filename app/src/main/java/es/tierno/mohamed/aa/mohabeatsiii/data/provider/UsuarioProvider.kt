package es.tierno.mohamed.aa.mohabeatsiii.data.provider

import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.UsuarioEntity

//Provider para cargar los datos a la base de datos
object UsuarioProvider {
    val usuarios = listOf(
        UsuarioEntity(
            id = 1,
            nombre = "Moha",
            usuario = "moha",
            contrasena = "123"
        ),
        UsuarioEntity(
            id = 2,
            nombre = "Ali",
            usuario = "ali",
            contrasena = "123"
        )
    )
}