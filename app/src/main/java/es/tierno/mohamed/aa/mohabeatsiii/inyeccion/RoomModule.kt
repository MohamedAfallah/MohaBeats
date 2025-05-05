package es.tierno.mohamed.aa.mohabeatsiii.inyeccion

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.tierno.mohamed.aa.mohabeatsiii.data.db.MohaBeatsDataBase
import javax.inject.Singleton

// La inyeccion del Room
@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    const val NOMBRE_DB = "moha_beats"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(context,
        MohaBeatsDataBase::class.java, NOMBRE_DB).build()

    @Singleton
    @Provides
    fun provideGetCanciones(db: MohaBeatsDataBase) = db.musicaDao()

    @Singleton
    @Provides
    fun providesGetUsuarios(db: MohaBeatsDataBase) = db.usuarioDao()

    @Singleton
    @Provides
    fun providesGetFavoritos(db: MohaBeatsDataBase) = db.musicaFavoritaDao()
}