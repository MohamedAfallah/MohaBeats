package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist

import es.tierno.mohamed.aa.mohabeatsiii.data.PlaylistRepositorio
import javax.inject.Inject

class AnadirCancionAPlaylistUseCase@Inject
constructor(
    private val repositorio:PlaylistRepositorio
) {
    suspend operator fun invoke(idCancoin : String, idUsuario:String, idPlaylist: String){
        repositorio.agregarCancion(idCancoin, idUsuario, idPlaylist)
    }
}