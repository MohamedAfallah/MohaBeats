package es.tierno.mohamed.aa.mohabeatsiii.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoriasModel(
    val nombre: String,
    val urlImagen: String,
    val colorSuperposicion: Int,
    val cod: String
) : Parcelable