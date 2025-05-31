package es.tierno.mohamed.aa.mohabeatsiii.inyeccion

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.tierno.mohamed.aa.mohabeatsiii.core.RetrofitCreator
import es.tierno.mohamed.aa.mohabeatsiii.data.network.musica_api.ITunesApi
import es.tierno.mohamed.aa.mohabeatsiii.data.network.musica_api.ITunesServicio
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return RetrofitCreator.getRetrofit()
    }

    @Provides
    @Singleton
    fun provideITunesApi(retrofit: Retrofit): ITunesApi {
        return retrofit.create(ITunesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideITunesServicio(api: ITunesApi): ITunesServicio {
        return ITunesServicio(api)
    }
}