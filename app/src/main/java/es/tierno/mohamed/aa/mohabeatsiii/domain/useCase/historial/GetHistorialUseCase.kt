package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.historial

import es.tierno.mohamed.aa.mohabeatsiii.data.HistorialRepositorio
import javax.inject.Inject

class GetHistorialUseCase @Inject constructor(
    private val repositorio: HistorialRepositorio
) {
    suspend operator fun invoke(id:String): List<String>?{
        return repositorio.getHistorial(id)
    }
}