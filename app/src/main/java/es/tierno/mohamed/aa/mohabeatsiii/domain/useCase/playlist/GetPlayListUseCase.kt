package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist

import es.tierno.mohamed.aa.mohabeatsiii.data.PlaylistRepositorio
import javax.inject.Inject

class GetPlayListUseCase@Inject
constructor(
    private val repositorio:PlaylistRepositorio
) {
    suspend operator fun invoke(idUsuario: String) = repositorio.getPlaylists(idUsuario)
}