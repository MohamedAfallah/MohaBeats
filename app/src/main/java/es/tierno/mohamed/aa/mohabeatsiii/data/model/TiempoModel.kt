package es.tierno.mohamed.aa.mohabeatsiii.data.model

import com.google.gson.annotations.SerializedName

data class TiempoModel(@SerializedName("title") val nombreCiudad:String,
                       @SerializedName("today") val tiempoHoy: Any,
                       @SerializedName("tomorrow") val tiempoDiaSiguiente: Any)