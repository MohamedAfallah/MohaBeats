package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.descargas

import es.tierno.mohamed.aa.mohabeatsiii.data.DescargasRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import javax.inject.Inject

class GetTodasLasCancionesDescargasUseCase @Inject constructor(
    private val repositorio: DescargasRepositorio
){
    suspend operator fun invoke(): List<Musica>{
        return repositorio.getTodasLasCancionesDescargadas()
    }
}