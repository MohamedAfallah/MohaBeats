package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.mapper.toMusica
import es.tierno.mohamed.aa.mohabeatsiii.data.db.dao.MusicaDao
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaEntity

import javax.inject.Inject

class MusicaRepositorio@Inject constructor (
    private val musicaDao: MusicaDao) {

    suspend fun getCanciones(): List<Musica> {
        val response = musicaDao.obtenerMusica()
        return response.map { it.toMusica() }
    }

    suspend fun insertarCanciones(musica: List<Musica>) {
        val musicaEntities = musica.map {
            MusicaEntity(
                nombre = it.nombre,
                artista = it.artista,
                url = it.url
            )
        }
        musicaDao.insertarMusica(musicaEntities)
    }
}