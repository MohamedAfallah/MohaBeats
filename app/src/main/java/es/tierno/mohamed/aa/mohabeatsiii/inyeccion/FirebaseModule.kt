package es.tierno.mohamed.aa.mohabeatsiii.inyeccion

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.tierno.mohamed.aa.mohabeatsiii.core.FbAuth
import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.dao.FirestoreUsuarioDAO
import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.historial.HistorialDAO
import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.historial.ProviderHistorialDAO
import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.playlist.PlaylistDAO
import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.playlist.ProviderPlaylist
import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.posts.PostDAO
import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.posts.ProviderPostDAO
import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.usuario.UsuarioDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideUsuarioRemoteDataSource(
        firestore: FirebaseFirestore
    ): UsuarioDAO =
        FirestoreUsuarioDAO(firestore)

    @Provides
    @Singleton
    fun provideHistorialRemoteDataSource(
        firestore: FirebaseFirestore
    ): HistorialDAO =
        ProviderHistorialDAO(firestore)

    @Provides
    @Singleton
    fun providePlaylistlRemoteDataSource(
        firestore: FirebaseFirestore
    ): PlaylistDAO =
        ProviderPlaylist(firestore)

    @Provides
    @Singleton
    fun provideFbAuth(): FbAuth {
        return FbAuth(/* si requiere params, aquí los pasas */)
    }

    @Provides
    @Singleton
    fun providePostlRemoteDataSource(
        firestore: FirebaseFirestore
    ): PostDAO =
        ProviderPostDAO(firestore)
}
