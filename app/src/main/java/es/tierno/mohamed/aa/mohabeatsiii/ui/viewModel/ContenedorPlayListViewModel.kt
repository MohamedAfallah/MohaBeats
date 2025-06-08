package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Playlist
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist.EliminarPlayListUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist.GetPlayListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContenedorPlayListViewModel @Inject constructor(
    private val getPlayListUseCase: GetPlayListUseCase,
    private val eliminarPlayListUseCase: EliminarPlayListUseCase
) : ViewModel() {
    val playlists = MutableLiveData<List<Playlist>>()
    val deleteResult = MutableLiveData<Boolean>()

    fun getPlaylits(idUsuario: String) {
        viewModelScope.launch {
            try {
                val listasDeReproduccion = getPlayListUseCase(idUsuario)
                playlists.postValue(listasDeReproduccion)
            } catch (e: Exception) {
                playlists.postValue(emptyList())
            }
        }
    }

    fun eliminarPlaylist(idPlaylist: String, idUsuario: String) {
        viewModelScope.launch {
            try {
                eliminarPlayListUseCase(idPlaylist, idUsuario)
                deleteResult.postValue(true)
                getPlaylits(idUsuario)
            } catch (e: Exception) {
                deleteResult.postValue(false)
            }
        }
    }
}