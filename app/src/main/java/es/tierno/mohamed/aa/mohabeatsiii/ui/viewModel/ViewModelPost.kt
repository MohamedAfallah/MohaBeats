package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Playlist
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Post
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.PostConPlaylist
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.playlist.GetUnaPlayListPorIdUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.posts.GetPostUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.posts.GetPostsPorIdUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.usuario.GetUsuarioPorIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelPost @Inject constructor(
    private val getPostsPorIdUseCase: GetPostsPorIdUseCase,
    private val getUnaPlayListPorIdUsecase: GetUnaPlayListPorIdUseCase,
    private val getPosts : GetPostUseCase,
    private val getUsuarioPorIdUseCase: GetUsuarioPorIdUseCase
) : ViewModel() {

    private val _postsConPlaylist = MutableStateFlow<List<PostConPlaylist>>(emptyList())
    val postsConPlaylist: StateFlow<List<PostConPlaylist>> = _postsConPlaylist.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _postCreationResult = MutableStateFlow<Boolean?>(null)
    val postCreationResult: StateFlow<Boolean?> = _postCreationResult.asStateFlow()

    private val _postById = MutableStateFlow<List<PostConPlaylist>>(emptyList())
    val postById: StateFlow<List<PostConPlaylist>> = _postById.asStateFlow()

    private val _allPosts = MutableStateFlow<List<PostConPlaylist>>(emptyList())
    val allPosts: StateFlow<List<PostConPlaylist>> = _allPosts.asStateFlow()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

                if (currentUserId.isNullOrEmpty()) {
                    _error.value = "No se ha encontrado un usuario autenticado para cargar los posts."
                    _isLoading.value = false
                    return@launch
                }

                val rawPosts: List<Post> = getPostsPorIdUseCase.invoke(currentUserId)
                _postsConPlaylist.value = enrichPosts(rawPosts)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al cargar los posts del usuario."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun enrichPosts(rawPosts: List<Post>): List<PostConPlaylist> {
        return rawPosts.mapNotNull { post ->
            val playlist = if (post.playlist.isNullOrEmpty()) {
                Playlist(
                    id = "no_disponible",
                    nombre = "Playlist no disponible",
                    canciones = emptyList()
                )
            } else {
                getUnaPlayListPorIdUsecase.invoke(post.playlist)
                    ?: Playlist(
                        id = "no_disponible",
                        nombre = "Playlist no encontrada",
                        canciones = emptyList()
                    )
            }

            val usuario = if (post.idUsuario.isNullOrEmpty()) {
                null
            } else {
                getUsuarioPorIdUseCase.invoke(post.idUsuario)
            }

            if (usuario != null) {
                PostConPlaylist(
                    id = post.id,
                    usuario = usuario,
                    comentario = post.comentario,
                    playlist = playlist
                )
            } else {
                null
            }
        }
    }

    fun refreshPosts() {
        loadPosts()
    }

    fun resetPostCreationResult() {
        _postCreationResult.value = null
    }

    fun devolverPostPorId(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _postById.value = emptyList()
            try {
                val rawPosts: List<Post> = getPostsPorIdUseCase.invoke(userId)
                _postById.value = enrichPosts(rawPosts)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al obtener posts del usuario."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun todosLosPostsQueHay() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _allPosts.value = emptyList()
            try {
                val rawPosts = getPosts()
                _allPosts.value = enrichPosts(rawPosts)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al obtener todos los posts."
            } finally {
                _isLoading.value = false
            }
        }
    }
}