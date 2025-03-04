package es.tierno.mohamed.aa.mohabeatsiii.data.mapper

import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaEntity
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica

fun MusicaEntity.toMusica(): Musica {
    return Musica(
        nombre = this.nombre,
        artista = this.artista,
        url = this.url
    )
}