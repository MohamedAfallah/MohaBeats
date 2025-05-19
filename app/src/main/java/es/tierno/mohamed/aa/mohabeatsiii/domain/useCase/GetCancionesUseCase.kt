package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase

import es.tierno.mohamed.aa.mohabeatsiii.data.MusicaRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.data.mapper.toMusica
import es.tierno.mohamed.aa.mohabeatsiii.data.provider.MusicaProvider
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import javax.inject.Inject

//Caso de uso para obtener las canciones
class GetCancionesUseCase @Inject constructor(
    private val repositorio: MusicaRepositorio
){
    suspend operator fun invoke() : List<Musica>{
        var musica = repositorio.getCanciones()

        if(musica.isNullOrEmpty()){
            repositorio.insertarCanciones(MusicaProvider.canciones.map {it.toMusica()})
            musica = repositorio.getCanciones()
        }
        return musica
    }
}