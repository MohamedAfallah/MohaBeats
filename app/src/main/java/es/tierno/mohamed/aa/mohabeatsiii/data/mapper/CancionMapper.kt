package es.tierno.mohamed.aa.mohabeatsiii.data.mapper

import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.CancionDescargadaEntidad
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica

fun Musica.toCancionDescargadaEntidad(): CancionDescargadaEntidad {
    return CancionDescargadaEntidad(
        idCancion = this.idCancion,
        nombreCancion = this.nombreCancion,
        idArtista = this.idArtista,
        nombreArtista = this.nombreArtista,
        idAlbum = this.idAlbum,
        nombreAlbum = this.nombreAlbum,
        urlImagen = this.urlImagen,
        urlPreview = this.urlPreview,
        genero = this.genero,
        duracionMillis = this.duracionMillis,
        fechaLanzamiento = this.fechaLanzamiento,
        rutaLocalCancion = this.rutaLocalCancion,
        rutaLocalImg = this.rutaLocalImg
    )
}

fun CancionDescargadaEntidad.toMusica(): Musica {
    return Musica(
        idCancion = this.idCancion,
        nombreCancion = this.nombreCancion,
        idArtista = this.idArtista,
        nombreArtista = this.nombreArtista,
        idAlbum = this.idAlbum,
        nombreAlbum = this.nombreAlbum,
        urlImagen = this.urlImagen,
        urlPreview = this.urlPreview,
        genero = this.genero,
        duracionMillis = this.duracionMillis,
        fechaLanzamiento = this.fechaLanzamiento,
        rutaLocalCancion = this.rutaLocalCancion,
        rutaLocalImg = this.rutaLocalImg
    )
}