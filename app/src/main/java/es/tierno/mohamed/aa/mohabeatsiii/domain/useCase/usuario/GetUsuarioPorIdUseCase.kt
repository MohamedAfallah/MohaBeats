package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.usuario

import es.tierno.mohamed.aa.mohabeatsiii.data.UsuarioRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import javax.inject.Inject

class GetUsuarioPorIdUseCase @Inject constructor(
    private val repositorio: UsuarioRepositorio
) {
    suspend operator fun invoke(id: String): Usuario? {
        return repositorio.obtenerUsuario(id)
    }
}