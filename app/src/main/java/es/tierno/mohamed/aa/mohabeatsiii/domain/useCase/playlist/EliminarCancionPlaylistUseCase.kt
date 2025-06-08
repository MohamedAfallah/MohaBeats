package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist

import es.tierno.mohamed.aa.mohabeatsiii.data.PlaylistRepositorio
import javax.inject.Inject

class EliminarCancionPlaylistUseCase@Inject
constructor(
    private val repositorio:PlaylistRepositorio
) {
    suspend operator fun invoke(idCancion: String, idUsuario: String, idPlaylist: String) {
        repositorio.eliminarCancion(idCancion, idUsuario, idPlaylist)
    }
}