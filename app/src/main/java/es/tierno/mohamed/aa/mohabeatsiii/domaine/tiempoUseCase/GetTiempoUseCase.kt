package es.tierno.mohamed.aa.mohabeatsiii.domaine.tiempoUseCase

import es.tierno.mohamed.aa.mohabeatsiii.data.TiempoRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.data.model.Tiempo

class GetTiempoUseCase {
    private val repositorio = TiempoRepositorio()

    suspend operator fun invoke(): Tiempo? = repositorio.getTiempo()
}