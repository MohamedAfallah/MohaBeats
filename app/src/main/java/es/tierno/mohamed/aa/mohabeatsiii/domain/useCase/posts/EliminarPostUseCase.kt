package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.posts

import es.tierno.mohamed.aa.mohabeatsiii.data.PostRepositorio
import javax.inject.Inject

class EliminarPostUseCase@Inject constructor(
    private val repositorio: PostRepositorio
) {
    suspend operator fun invoke(idUsuario : String, idPost: String){
        repositorio.eliminarPost(idUsuario, idPost)
    }
}