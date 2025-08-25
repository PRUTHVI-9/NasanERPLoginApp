package com.example.myapplication.di

import com.example.myapplication.data.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {

        val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // Trust all certificates
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // Trust all certificates
            }

            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return emptyArray()
            }
        })

        val sslContext = SSLContext.getInstance("TLS")
        try {
            sslContext.init(null, trustAllCertificates, java.security.SecureRandom())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        val sslSocketFactory = sslContext.socketFactory
        val hostnameVerifier = HostnameVerifier { _, _ -> true }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCertificates[0] as X509TrustManager)
            .hostnameVerifier(hostnameVerifier)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Time to establish a connection
            .readTimeout(30, TimeUnit.SECONDS)    // Time to wait for data after connection
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)

            .baseUrl("http://192.168.100.149/")
//            .baseUrl("http://192.168.100.82/")
//            .baseUrl("http://192.168.100.105/")
//            .baseUrl("http://192.168.100.90/")
//            .baseUrl("https://123.201.117.218:10/")
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}