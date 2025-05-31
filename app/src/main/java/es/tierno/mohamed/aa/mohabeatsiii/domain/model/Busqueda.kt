package es.tierno.mohamed.aa.mohabeatsiii.domain.model

import com.google.gson.annotations.SerializedName
import es.tierno.mohamed.aa.mohabeatsiii.data.model.MusicaModel

data class Busqueda(
    @SerializedName("resultCount") val total: Int,
    @SerializedName("results") val resultados: List<Musica>)