package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.posts

import es.tierno.mohamed.aa.mohabeatsiii.data.PostRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Post
import javax.inject.Inject

class GetPostUseCase@Inject constructor(
    private val repositorio: PostRepositorio
) {
    suspend operator fun invoke() : List<Post> = repositorio.getPost()
}