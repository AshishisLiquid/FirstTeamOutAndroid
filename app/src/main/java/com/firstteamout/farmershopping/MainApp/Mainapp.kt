package com.firstteamout.farmershopping.MainApp

import android.app.Application
import android.util.Log
import com.firstteamout.farmershopping.Miscs.Base_Url
import com.firstteamout.farmershopping.`interface`.FarmerClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Mainapp : Application(){
    lateinit var farmerApi: FarmerClient
    override fun onCreate() {
        super.onCreate()
        initialization()
    }

    private fun initialization() {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val retrofit = Retrofit.Builder()
            .baseUrl(Base_Url)
            .client(OkHttpClient.Builder().addInterceptor(logging).addInterceptor(RetryInterceptor()).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        farmerApi = retrofit.create(FarmerClient::class.java)
    }

    class RetryInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            var response = chain.proceed(request)
            var tryCount = 0
            val maxCount = 3

            while (!response.isSuccessful && tryCount < maxCount){
                Log.d("intercept", "Request failed - $tryCount")
                tryCount++
                response = chain.proceed(request)
            }
            return  response
        }

    }
}