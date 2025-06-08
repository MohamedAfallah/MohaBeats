package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist

import es.tierno.mohamed.aa.mohabeatsiii.data.PlaylistRepositorio
import javax.inject.Inject

class GetPlayListPorId@Inject constructor(
    private val repositorio: PlaylistRepositorio
) {
    suspend operator fun invoke(idPlaylist : String, idUsuario: String) = repositorio.getPlaylistPorId(idPlaylist, idUsuario)
}