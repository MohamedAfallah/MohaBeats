package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos

import es.tierno.mohamed.aa.mohabeatsiii.data.FavoritosRepositorio
import javax.inject.Inject

class EliminarCancionUseCase @Inject constructor(
    private val repositorio: FavoritosRepositorio
){
    suspend operator fun invoke(id : String, id_cancion: String){
        repositorio.eliminarFavoritas(id, id_cancion)
    }
}