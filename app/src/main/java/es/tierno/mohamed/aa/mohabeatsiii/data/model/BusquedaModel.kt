package es.tierno.mohamed.aa.mohabeatsiii.data.model

import com.google.gson.annotations.SerializedName

data class BusquedaModel(
    @SerializedName("resultCount") val total: Int,
    @SerializedName("results") val resultados: List<MusicaModel>
)