package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.vm_bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Playlist
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist.AnadirCancionAPlaylistUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist.AnadirPlayListUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist.GetPlayListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BtmShtPlaylistViewModel @Inject constructor(
    private val getPlayListUseCase: GetPlayListUseCase,
    private val anadirCancionAPlaylistUseCase: AnadirCancionAPlaylistUseCase,
    private val crearPlaylistUseCase: AnadirPlayListUseCase // Inyectado
) : ViewModel() {

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    private val _resultadoAnadirCancion = MutableLiveData<Boolean>()
    val resultadoAnadirCancion: LiveData<Boolean> = _resultadoAnadirCancion

    private val _resultadoCreacionYAnadido = MutableLiveData<Boolean>()
    val resultadoCreacionYAnadido: LiveData<Boolean> = _resultadoCreacionYAnadido

    fun cargarPlaylists(idUsuario: String) {
        viewModelScope.launch {
            try {
                val playlistsUsuario = getPlayListUseCase(idUsuario)
                _playlists.value = playlistsUsuario
            } catch (e: Exception) {
                _playlists.value = emptyList()
            }
        }
    }

    fun anadirCancionAPlaylist(idPlaylist: String, idUsuario: String, idCancion: String) {
        viewModelScope.launch {
            try {
                anadirCancionAPlaylistUseCase(idCancion, idUsuario, idPlaylist)
                _resultadoAnadirCancion.value = true
            } catch (e: Exception) {
                _resultadoAnadirCancion.value = false
            }
        }
    }

    fun crearNuevaPlaylistYAnadirCancion(userId: String, playlistName: String, songId: String) {
        viewModelScope.launch {
            try {
                val nuevaPlaylist = Playlist(
                    id = "",
                    nombre = playlistName,
                    canciones = listOf(songId)
                )
                crearPlaylistUseCase(nuevaPlaylist, userId)
                _resultadoCreacionYAnadido.value = true
                cargarPlaylists(userId)
            } catch (e: Exception) {
                _resultadoCreacionYAnadido.value = false
            }
        }
    }
}