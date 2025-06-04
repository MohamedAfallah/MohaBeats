package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import android.util.Log // Importar Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.GetCancionesUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.AnadirCancionUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.EliminarCancionUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.GetFavoritosUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicaViewModel @Inject constructor (
    private val getCancionesUseCase: GetCancionesUseCase,
    private val getFavoritosUseCase: GetFavoritosUseCase,
    private val anadirCancionUseCase: AnadirCancionUseCase,
    private val eliminarCancionUseCase: EliminarCancionUseCase
) : ViewModel() {

    private var todasCanciones = listOf<Musica>()
    private val paginaSize = 10
    private var paginaActual = 0

    val musicaPaginada = MutableLiveData<List<Musica>>()
    val favoritasIds = MutableLiveData<Set<String>>(emptySet())

    fun onCreate(idUsuario: String) {
        viewModelScope.launch {
            Log.d("FavoritosDebug", "ViewModel: onCreate(${idUsuario}) llamado.")
            try {
                todasCanciones = getCancionesUseCase()
                Log.d("FavoritosDebug", "ViewModel: Total de canciones cargadas: ${todasCanciones.size}")
            } catch (e: Exception) {
                Log.e("FavoritosDebug", "ViewModel: Error al cargar todas las canciones: ${e.message}")
                todasCanciones = emptyList()
            }
            paginaActual = 0
            cargarPagina()
            cargarFavoritos(idUsuario)
        }
    }

    fun cargarPagina() {
        val fromIndex = paginaActual * paginaSize
        val toIndex = (fromIndex + paginaSize).coerceAtMost(todasCanciones.size)
        if (fromIndex < toIndex) {
            val pagina = todasCanciones.subList(0, toIndex)
            musicaPaginada.postValue(pagina)
            paginaActual++
            Log.d("FavoritosDebug", "ViewModel: Página cargada: de $fromIndex a $toIndex. Total en musicaPaginada: ${pagina.size}")
        } else {
            Log.d("FavoritosDebug", "ViewModel: No hay más páginas para cargar. fromIndex: $fromIndex, toIndex: $toIndex, totalCanciones: ${todasCanciones.size}")
        }
    }

    private fun cargarFavoritos(idUsuario: String) {
        viewModelScope.launch {
            Log.d("FavoritosDebug", "ViewModel: Iniciando cargarFavoritos para usuario: $idUsuario")
            if (idUsuario.isNotEmpty() && idUsuario != "invitado") {
                try {
                    val ids = getFavoritosUseCase(idUsuario)?.toSet() ?: emptySet()
                    favoritasIds.postValue(ids)
                    Log.d("FavoritosDebug", "ViewModel: Favoritos cargados. Cantidad: ${ids.size}. IDs: $ids")
                } catch (e: Exception) {
                    Log.e("FavoritosDebug", "ViewModel: Error al cargar favoritos de ${idUsuario}: ${e.message}")
                    favoritasIds.postValue(emptySet()) // Asegura un estado consistente en caso de error
                }
            } else {
                favoritasIds.postValue(emptySet())
                Log.d("FavoritosDebug", "ViewModel: Usuario invitado o ID vacío, favoritos establecidos a vacío.")
            }
        }
    }

    fun anadirAFavoritos(idUsuario: String, idCancion: String) {
        viewModelScope.launch {
            Log.d("FavoritosDebug", "ViewModel: Llamando anadirAFavoritos para idCancion: $idCancion por idUsuario: $idUsuario")
            try {
                anadirCancionUseCase(idUsuario, idCancion)
                Log.d("FavoritosDebug", "ViewModel: anadirCancionUseCase ejecutado para $idCancion.")
            } catch (e: Exception) {
                Log.e("FavoritosDebug", "ViewModel: Error en anadirCancionUseCase para $idCancion: ${e.message}")
            }
            cargarFavoritos(idUsuario) // Esto recarga la lista de favoritos
            Log.d("FavoritosDebug", "ViewModel: anadirAFavoritos completado, solicitando recarga de favoritos.")
        }
    }

    fun eliminarDeFavoritos(idUsuario: String, idCancion: String) {
        viewModelScope.launch {
            Log.d("FavoritosDebug", "ViewModel: Llamando eliminarDeFavoritos para idCancion: $idCancion por idUsuario: $idUsuario")
            try {
                eliminarCancionUseCase(idUsuario, idCancion)
                Log.d("FavoritosDebug", "ViewModel: eliminarCancionUseCase ejecutado para $idCancion.")
            } catch (e: Exception) {
                Log.e("FavoritosDebug", "ViewModel: Error en eliminarCancionUseCase para $idCancion: ${e.message}")
            }
            cargarFavoritos(idUsuario) // Esto recarga la lista de favoritos
            Log.d("FavoritosDebug", "ViewModel: eliminarDeFavoritos completado, solicitando recarga de favoritos.")
        }
    }
}
