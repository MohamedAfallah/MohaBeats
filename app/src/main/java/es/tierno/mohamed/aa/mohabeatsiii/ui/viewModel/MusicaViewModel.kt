package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.GetCancionesUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.descargas.InsertarUnaDescarga
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.AnadirCancionUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.EliminarCancionUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.GetFavoritosUseCase
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MusicaViewModel @Inject constructor (
    private val app: Application,
    private val getCancionesUseCase: GetCancionesUseCase,
    private val getFavoritosUseCase: GetFavoritosUseCase,
    private val anadirCancionUseCase: AnadirCancionUseCase,
    private val eliminarCancionUseCase: EliminarCancionUseCase,
    private val insertarUnaDescarga: InsertarUnaDescarga
) : ViewModel() {

    private var todasCanciones = listOf<Musica>()
    private val paginaSize = 10
    private var paginaActual = 0

    val musicaPaginada = MutableLiveData<List<Musica>>()
    val favoritasIds = MutableLiveData<Set<String>>(emptySet())

    fun onCreate(idUsuario: String) {
        viewModelScope.launch {
            try {
                todasCanciones = getCancionesUseCase()
            } catch (e: Exception) {
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
        }
    }

    private fun cargarFavoritos(idUsuario: String) {
        viewModelScope.launch {
            if (idUsuario.isNotEmpty() && idUsuario != "invitado") {
                try {
                    val ids = getFavoritosUseCase(idUsuario)?.toSet() ?: emptySet()
                    favoritasIds.postValue(ids)
                } catch (e: Exception) {
                    favoritasIds.postValue(emptySet())
                }
            } else {
                favoritasIds.postValue(emptySet())
            }
        }
    }

    fun anadirAFavoritos(idUsuario: String, idCancion: String) {
        viewModelScope.launch {
            try {
                anadirCancionUseCase(idUsuario, idCancion)
            } catch (e: Exception) {
            }
            cargarFavoritos(idUsuario)
        }
    }

    fun eliminarDeFavoritos(idUsuario: String, idCancion: String) {
        viewModelScope.launch {
            try {
                eliminarCancionUseCase(idUsuario, idCancion)
            } catch (e: Exception) {
            }
            cargarFavoritos(idUsuario)
        }
    }

    fun descargarCancion(musica: Musica) {
        viewModelScope.launch {
            val downloadManager = app.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            musica.urlPreview?.let { audioUrl ->
                try {
                    val audioFileName = "${musica.idCancion}_${musica.nombreCancion}.mp3"
                    val audioFile = File(app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), audioFileName)

                    val requestAudio = DownloadManager.Request(Uri.parse(audioUrl)).apply {
                        setTitle("Descargando: ${musica.nombreCancion}")
                        setDescription("De ${musica.nombreArtista}")
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        setDestinationUri(Uri.fromFile(audioFile)) // Establece el URI de destino del archivo
                    }
                    downloadManager.enqueue(requestAudio)
                    musica.rutaLocalCancion = audioFile.absolutePath // Guarda la ruta absoluta
                } catch (e: Exception) {
                }
            }

            musica.urlImagen.let { imageUrl ->
                try {
                    val imageFileName = "${musica.idCancion}_${musica.nombreCancion}_artwork.jpg"
                    val imageFile = File(app.getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName)

                    val requestImage = DownloadManager.Request(Uri.parse(imageUrl)).apply {
                        setTitle("Descargando imagen de: ${musica.nombreCancion}")
                        setDescription("Artwork de ${musica.nombreArtista}")
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                        setDestinationUri(Uri.fromFile(imageFile)) // Establece el URI de destino del archivo
                    }
                    downloadManager.enqueue(requestImage)
                    musica.rutaLocalImg = imageFile.absolutePath // Guarda la ruta absoluta
                } catch (e: Exception) {
                }
            }

            try {
                insertarUnaDescarga.invoke(musica)
            } catch (e: Exception) {
            }
        }
    }
}
