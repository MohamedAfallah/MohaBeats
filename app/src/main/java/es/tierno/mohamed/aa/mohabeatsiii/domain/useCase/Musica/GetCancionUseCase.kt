package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica

import es.tierno.mohamed.aa.mohabeatsiii.data.MusicaRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import javax.inject.Inject

class GetCancionUseCase @Inject constructor(
    private val repositorio: MusicaRepositorio
) {
    suspend operator fun invoke(id : String): Musica?{
        return repositorio.obtenerCancion(id)
    }
}