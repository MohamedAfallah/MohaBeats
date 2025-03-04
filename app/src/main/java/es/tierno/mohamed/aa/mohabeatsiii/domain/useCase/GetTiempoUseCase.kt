package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase

import es.tierno.mohamed.aa.mohabeatsiii.data.TiempoRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.data.model.TiempoModel

// caso de uso para obtener el tiempo que hace.
class GetTiempoUseCase {
    private val repositorio = TiempoRepositorio()

    suspend operator fun invoke(): TiempoModel? = repositorio.getTiempo()
}