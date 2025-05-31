package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica

import es.tierno.mohamed.aa.mohabeatsiii.data.MusicaRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import javax.inject.Inject

class GetCancionesUseCase @Inject constructor(
    private val repositorio: MusicaRepositorio
) {
    suspend operator fun invoke(): List<Musica> {
        return repositorio.obtenerCanciones()
    }
}
