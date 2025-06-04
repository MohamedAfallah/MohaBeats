package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos

import es.tierno.mohamed.aa.mohabeatsiii.data.FavoritosRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import javax.inject.Inject

//Caso de uso para obtener las canciones favoritas de un usuario x
class GetFavoritosUseCase @Inject constructor(
    private val repositorio: FavoritosRepositorio
) {
    suspend operator fun invoke(id: String): List<String>? = repositorio.getFavoritas(id)
}