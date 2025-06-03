package es.tierno.mohamed.aa.mohabeatsiii.core

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitCreator {
    private const val BASE_URL = "https://itunes.apple.com/"

    fun getRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val request: Request = original.newBuilder()
                    .header("User-Agent", "PostmanRuntime/7.32.2")
                    .header("Accept", "*/*")
                    .header("Connection", "keep-alive")
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}