package es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.playlist

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Playlist
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProviderPlaylist @Inject constructor(
    private val firestore: FirebaseFirestore
) : PlaylistDAO {

    //Esto es para recuperar todas las colecciones con el nombre PlaylistCollection
    private fun playlistCollection(idUsuario: String) =
        firestore.collection("playlists")
            .document(idUsuario)
            .collection("PlaylistCollection")

    override suspend fun crearPlayist(playlist: Playlist, idUsuario: String) {
        val newPlaylistRef = playlistCollection(idUsuario).document()
        val dataToSave = mapOf(
            "id" to newPlaylistRef.id,
            "nombre" to playlist.nombre,
            "canciones" to playlist.canciones
        )
        newPlaylistRef.set(dataToSave).await()
    }

    override suspend fun agregarCancion(idCancion: String, idUsuario: String, idPlaylist: String) {
        val playlistRef = playlistCollection(idUsuario).document(idPlaylist)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(playlistRef)
            val canciones = snapshot.get("canciones") as? List<String> ?: emptyList()
            if (!canciones.contains(idCancion)) {
                val nuevasCanciones = canciones + idCancion
                transaction.update(playlistRef, "canciones", nuevasCanciones)
            }
        }.await()
    }

    override suspend fun eliminarCancion(idCancion: String, idUsuario: String, idPlaylist: String) {
        Log.d("PSG", "AQUI ENTRO" + idCancion +" " + idUsuario + " "+ idPlaylist)

        val playlistRef = playlistCollection(idUsuario).document(idPlaylist)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(playlistRef)
            val canciones = snapshot.get("canciones") as? List<String> ?: emptyList()
            val nuevasCanciones = canciones.filterNot { it == idCancion }
            transaction.update(playlistRef, "canciones", nuevasCanciones)

        }.await()
    }

    override suspend fun eliminarPlaylist(playlistId: String, idUsuario: String) {
        playlistCollection(idUsuario)
            .document(playlistId)
            .delete()
            .await()
    }

    override suspend fun getPlaylists(idUsuario: String): List<Playlist> {
        val snapshot = playlistCollection(idUsuario).get().await()

        return snapshot.documents.mapNotNull { doc ->
            val nombre = doc.getString("nombre") ?: return@mapNotNull null
            val idCanciones = doc.get("canciones") as? List<String> ?: emptyList()

            Playlist(
                id = doc.id,
                nombre = nombre,
                canciones = idCanciones
            )
        }
    }

    override suspend fun getPlaylistPorId(idPlaylist: String, idUsuario: String): Playlist {
        val docSnapshot = playlistCollection(idUsuario)
            .document(idPlaylist)
            .get()
            .await()

        return if (docSnapshot.exists()) {
            val nombre = docSnapshot.getString("nombre")
            val canciones = docSnapshot.get("canciones") as? List<String>

            if (nombre != null && canciones != null) {
                Playlist(
                    id = docSnapshot.id,
                    nombre = nombre,
                    canciones = canciones
                )
            } else {
                Playlist(id = idPlaylist, nombre = "", canciones = emptyList())
            }
        } else {
            Playlist(id = idPlaylist, nombre = "", canciones = emptyList())
        }
    }

    override suspend fun getPlayListPorIdPlaylist(idPlaylist: String): Playlist? {
        if (idPlaylist.isEmpty()) {
            return null
        }
        val querySnapshot = firestore.collectionGroup("PlaylistCollection")
            .whereEqualTo("id", idPlaylist)
            .limit(1)
            .get()
            .await()

        Log.d("PSG", querySnapshot.toString())

        return if (querySnapshot.documents.isNotEmpty()) {
            val doc = querySnapshot.documents.first()
            val nombre = doc.getString("nombre")
            val canciones = doc.get("canciones") as? List<String>

            Log.d("PSG", doc.toString())
            Log.d("PSG", nombre.toString())


            if (nombre != null && canciones != null) {
                Playlist(
                    id = doc.id,
                    nombre = nombre,
                    canciones = canciones
                )
            } else {
                null
            }
        } else {
            null
        }
    }
}


