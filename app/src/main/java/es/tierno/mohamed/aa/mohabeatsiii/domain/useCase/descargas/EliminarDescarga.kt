package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.descargas

import es.tierno.mohamed.aa.mohabeatsiii.data.DescargasRepositorio
import javax.inject.Inject

class EliminarDescarga@Inject constructor(
    private val repositorio: DescargasRepositorio
) {
    suspend operator fun invoke(cancionID : String){
        repositorio.eliminarCancion(cancionID)
    }
}