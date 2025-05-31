package es.tierno.mohamed.aa.mohabeatsiii.data.model

import com.google.gson.annotations.SerializedName
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica

data class BusquedaModel(
    @SerializedName("resultCount") val total: Int,
    @SerializedName("results") val resultados: List<MusicaModel>
)