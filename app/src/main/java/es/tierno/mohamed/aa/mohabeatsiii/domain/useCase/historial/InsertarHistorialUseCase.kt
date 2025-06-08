package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.historial

import es.tierno.mohamed.aa.mohabeatsiii.data.HistorialRepositorio
import javax.inject.Inject

class InsertarHistorialUseCase@Inject constructor(
    private val repositorio: HistorialRepositorio
) {
    suspend operator fun invoke(id: String, idCancion :String){
        repositorio.insertarHistorial(id, idCancion)
    }
}