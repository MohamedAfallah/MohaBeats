package es.tierno.mohamed.aa.mohabeatsiii.inyeccion

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.tierno.mohamed.aa.mohabeatsiii.data.network.open_ai.OpenAiServicio

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOpenAiServicio(): OpenAiServicio {
        return OpenAiServicio()
    }
}