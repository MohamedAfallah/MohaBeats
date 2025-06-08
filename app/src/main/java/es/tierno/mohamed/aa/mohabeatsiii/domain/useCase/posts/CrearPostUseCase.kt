package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.posts

import es.tierno.mohamed.aa.mohabeatsiii.data.PostRepositorio
import javax.inject.Inject

class CrearPostUseCase@Inject constructor(
    private val repositorio: PostRepositorio
) {
    suspend operator fun invoke(idUsuario : String, comentario : String, idPlayList : String) {
        repositorio.crearPost(idUsuario,  comentario, idPlayList)
    }

}