package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.db.dao.MusicaDao
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica


import javax.inject.Inject

//Repositorio para el control de data de las canciones
class MusicaRepositorio@Inject constructor (
    private val musicaDao: MusicaDao) {

    suspend fun getCanciones() {

    }

    suspend fun insertarCanciones(musica: List<Musica>) {

    }
}