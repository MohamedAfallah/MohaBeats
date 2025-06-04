package es.tierno.mohamed.aa.mohabeatsiii.domain.model

import com.google.firebase.firestore.DocumentId

data class Usuario(
    @DocumentId
    var id: String = "",
    var nombreCompleto: String = "",
    var fechaNacimiento: String = "",
    var correo: String = "",
    var usuario: String = "",
    var telefono: String = "",
    var contrasena: String = ""
)
