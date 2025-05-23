package es.tierno.mohamed.aa.mohabeatsiii.data.model

data class PlaylistModel(val id: String,
                         val nombre: String,
                         val canciones: List<MusicaModel>)