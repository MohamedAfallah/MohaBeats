package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist.GetPlayListPorId
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Playlist
import kotlinx.coroutines.launch
import javax.inject.Inject
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.GetCancionUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist.EliminarCancionPlaylistUseCase

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val getPlaylistById: GetPlayListPorId,
    private val getCancionUseCase: GetCancionUseCase,
    private val eliminarCancionPlaylistUseCase: EliminarCancionPlaylistUseCase
) : ViewModel() {
    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> = _playlist

    fun getPlaylist(playlistId: String, userId: String) {
        viewModelScope.launch {
            try {
                val result = getPlaylistById(playlistId, userId)
                _playlist.value = result
            } catch (e: Exception) {
                _playlist.value = null
            }
        }
    }

    suspend fun obtenerCancionPorId(songIds: List<String>): List<Musica> {
        val songs = mutableListOf<Musica>()
        for (id in songIds) {
            val cancion = getCancionUseCase(id)
            if (cancion != null) {
                songs.add(cancion)
            }
        }
        return songs
    }

    fun eliminarCancionDePlaylist(playlistId: String, userId: String, songId: String) {
        viewModelScope.launch {
            try {
                eliminarCancionPlaylistUseCase(songId, userId, playlistId)
                getPlaylist(playlistId, userId) // Recargar la playlist después de eliminar
            } catch (e: Exception) {
                // Manejo de errores (sin logs/toasts según tu petición)
            }
        }
    }
}