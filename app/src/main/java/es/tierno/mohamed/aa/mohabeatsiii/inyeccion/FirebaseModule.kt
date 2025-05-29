package es.tierno.mohamed.aa.mohabeatsiii.inyeccion

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.tierno.mohamed.aa.mohabeatsiii.core.FbAuth
import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.dao.FirestoreUsuarioDAO
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
    fun provideFbAuth(): FbAuth {
        return FbAuth(/* si requiere params, aquí los pasas */)
    }
}
