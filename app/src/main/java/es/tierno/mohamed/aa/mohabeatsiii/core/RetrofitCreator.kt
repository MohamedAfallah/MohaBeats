package es.tierno.mohamed.aa.mohabeatsiii.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCreator {
    private const val BASE_URL_OPEN_AI = "https://api.openai.com/v1/"
    private const val BASE_URL_MUSICA_API = "https://api.openai.com/v1/"

    //Primero paso para poder conectarse a una API
    fun getRetrofit() : Retrofit{
        return Retrofit.Builder().baseUrl(BASE_URL_OPEN_AI)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}