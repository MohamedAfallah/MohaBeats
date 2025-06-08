package es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.playlist

import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Playlist

interface PlaylistDAO {
    suspend fun crearPlayist (playlist: Playlist, idUsuario : String)
    suspend fun agregarCancion(idCancion : String, idUsuario: String, idPlaylist : String)
    suspend fun eliminarCancion(idCancion : String, idUsuario: String, idPlaylist : String)
    suspend fun eliminarPlaylist(playlistId: String, idUsuario: String)
    suspend fun getPlaylists(idUsuario : String) : List<Playlist>
    suspend fun getPlaylistPorId(idPlaylist : String, idUsuario: String) : Playlist
    suspend fun getPlayListPorIdPlaylist(idPlaylist : String) : Playlist?
}