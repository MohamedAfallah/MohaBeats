package es.tierno.mohamed.aa.mohabeatsiii.data.provider

import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaFavoritaEntity

//Provider para cargar los datos a la base de datos
object FavoritosProvider {
    val favoritos = listOf(
        MusicaFavoritaEntity(
            usuarioId = 1,
            musicaId = 2
        ),
        MusicaFavoritaEntity(
        usuarioId = 1,
        musicaId = 3
        ),
        MusicaFavoritaEntity(
        usuarioId = 1,
        musicaId = 5    ),
        MusicaFavoritaEntity(
        usuarioId = 2,
        musicaId = 3
    ),
        MusicaFavoritaEntity(
        usuarioId = 2,
        musicaId = 4    )
    )
}