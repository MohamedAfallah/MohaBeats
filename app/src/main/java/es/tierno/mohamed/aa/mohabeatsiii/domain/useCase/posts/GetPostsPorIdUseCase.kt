package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.posts

import es.tierno.mohamed.aa.mohabeatsiii.data.PostRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Post
import javax.inject.Inject

class GetPostsPorIdUseCase@Inject constructor(
    private val repositorio: PostRepositorio
) {
    suspend operator fun invoke(idUsuario : String) : List<Post> = repositorio.getPostPorUsuario(idUsuario)

}