package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos

import es.tierno.mohamed.aa.mohabeatsiii.data.FavoritosRepositorio
import javax.inject.Inject

//Caso de uso para obtener las canciones favoritas de un usuario x
class GetFavoritosUseCase @Inject constructor(
    private val repositorio: FavoritosRepositorio
) {
    suspend operator fun invoke(id : Int) : List<Musica>{
        var musica = repositorio.getFavoritas(id)

        if(musica.isNullOrEmpty()){
            repositorio.insertarFavoritos(FavoritosProvider.favoritos)
            musica = repositorio.getFavoritas(id)
        }

        return musica
    }
}