package es.tierno.mohamed.aa.mohabeatsiii.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCreator {
    private const val BASE_URL = "https://itunes.apple.com/"

    // Crear una instancia de Retrofit configurada para la API de Deezer
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}