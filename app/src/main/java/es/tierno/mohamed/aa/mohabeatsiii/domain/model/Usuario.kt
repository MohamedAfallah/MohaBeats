package es.tierno.mohamed.aa.mohabeatsiii.domain.model

data class Usuario(
    val id: String,
    val nombreCompleto: String,
    val fechaNacimiento: String,
    val correo: String,
    val usuario: String,
    val telefono: String,
    val contrasena: String
)
