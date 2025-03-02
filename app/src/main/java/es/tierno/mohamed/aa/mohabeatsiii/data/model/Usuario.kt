package es.tierno.mohamed.aa.mohabeatsiii.data.model

data class Usuario(val nombre:String, val usuario:String, val contrasena:String, val musicaFavorita: List<Musica> = emptyList())
