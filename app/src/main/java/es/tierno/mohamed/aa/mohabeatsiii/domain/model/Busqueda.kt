package es.tierno.mohamed.aa.mohabeatsiii.domain.model

import com.google.gson.annotations.SerializedName

data class Busqueda(
    @SerializedName("resultCount") val total: Int,
    @SerializedName("results") val resultados: List<Musica>)