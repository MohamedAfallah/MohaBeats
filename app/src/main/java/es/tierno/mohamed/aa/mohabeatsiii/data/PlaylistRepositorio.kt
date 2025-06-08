package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.playlist.PlaylistDAO
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Playlist
import javax.inject.Inject

class PlaylistRepositorio @Inject constructor(
    private val playlistDAO: PlaylistDAO
) {
    suspend fun crearPlaylist(playlist: Playlist, idUsuario: String) {
        playlistDAO.crearPlayist(playlist, idUsuario)
    }

    suspend fun agregarCancion(idCancion: String, idUsuario: String, idPlaylist: String) {
        playlistDAO.agregarCancion(idCancion, idUsuario, idPlaylist)
    }

    suspend fun eliminarCancion(idCancion: String, idUsuario: String, idPlaylist: String) {
        playlistDAO.eliminarCancion(idCancion, idUsuario, idPlaylist)
    }

    suspend fun eliminarPlaylist(idPlaylist: String, idUsuario: String) {
        playlistDAO.eliminarPlaylist(idPlaylist, idUsuario)
    }

    suspend fun getPlaylistPorId(idPlaylist : String, idUsuario: String) : Playlist{
        return playlistDAO.getPlaylistPorId(idPlaylist, idUsuario)
    }

    suspend fun getPlaylists(idUsuario : String) : List<Playlist>{
        return playlistDAO.getPlaylists(idUsuario)
    }

    suspend fun getPlayListPorIdPlaylist(idPlaylist : String) : Playlist?{
        return playlistDAO.getPlayListPorIdPlaylist(idPlaylist)
    }
}
