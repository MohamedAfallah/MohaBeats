package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase

import es.tierno.mohamed.aa.mohabeatsiii.data.MusicaRepositorio
import javax.inject.Inject

//Caso de uso para obtener las canciones
class GetCancionesUseCase @Inject constructor(
    private val repositorio: MusicaRepositorio
){
    }