package es.tierno.mohamed.aa.mohabeatsiii.domain.model

import com.google.gson.annotations.SerializedName

data class Musica(
    @SerializedName("trackId") val idCancion: Long,
    @SerializedName("trackName") val nombreCancion: String,
    @SerializedName("artistId") val idArtista: Long,
    @SerializedName("artistName") val nombreArtista: String,
    @SerializedName("collectionId") val idAlbum: Long?,
    @SerializedName("collectionName") val nombreAlbum: String?,
    @SerializedName("artworkUrl100") val urlImagen: String,
    @SerializedName("previewUrl") val urlPreview: String?,
    @SerializedName("primaryGenreName") val genero: String,
    @SerializedName("trackTimeMillis") val duracionMillis: Long,
    @SerializedName("releaseDate") val fechaLanzamiento: String
)
