package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist

import es.tierno.mohamed.aa.mohabeatsiii.data.PlaylistRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Playlist
import javax.inject.Inject

class GetUnaPlayListPorIdUseCase @Inject constructor(
    private val repositorio: PlaylistRepositorio
) {
    suspend operator fun invoke(idPlayList : String) : Playlist? {
        return repositorio.getPlayListPorIdPlaylist(idPlayList)
    }
}