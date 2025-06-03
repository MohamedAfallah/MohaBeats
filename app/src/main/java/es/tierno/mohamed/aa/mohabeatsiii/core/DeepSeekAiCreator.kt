package es.tierno.mohamed.aa.mohabeatsiii.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DeepSeekAiCreator {
    private const val BASE_URL = "https://openrouter.ai/"

    // Crear una instancia de Retrofit configurada para la API de Deezer
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}