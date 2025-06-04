package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos

import es.tierno.mohamed.aa.mohabeatsiii.data.FavoritosRepositorio
import javax.inject.Inject

class AnadirCancionUseCase @Inject constructor(
    private val repositorio: FavoritosRepositorio
){
    suspend operator fun invoke(id: String, id_cancion: String){
        repositorio.insertarFavoritos(id, id_cancion)
    }
}