package com.globalmed.corelib.network

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.globalmed.corelib.BuildConfig
import com.globalmed.corelib.base.NetworkErrorListener
import com.globalmed.corelib.repository.RefreshTokenRepository
import com.globalmed.corelib.repository.SharedPreferenceRepositoryImplementation
import com.globalmed.corelib.request.RefreshTokenRequest
import com.globalmed.corelib.response.Refresh
import com.globalmed.corelib.response.RefreshTokenResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.LOGGING)
                level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideErrorListener(application: Application): NetworkErrorListener? {
        return application as NetworkErrorListener
    }

    @Provides
    fun provideResponseInterceptor(errorListener: NetworkErrorListener?): ResponseInterceptor {
        return ResponseInterceptor(errorListener)
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        responseInterceptor: ResponseInterceptor,
        application: Application,
        gsonConverterFactory: GsonConverterFactory
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor as Interceptor)
            .authenticator(TokenAuthenticator(application, gsonConverterFactory))
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        responseInterceptor.okHttpClient = okHttpClient
        return okHttpClient
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

    class TokenAuthenticator(
        context: Context,
        val gsonConverterFactory: GsonConverterFactory
    ) : Authenticator {
        val interceptor = HttpLoggingInterceptor().apply {
            HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        private val appContext = context.applicationContext
        private val userPreferences =
            SharedPreferenceRepositoryImplementation(application = appContext as Application)

        override fun authenticate(route: Route?, response: Response): Request? {
            return runBlocking {
                response.request().newBuilder().header("authorization", getUpdatedToken())
                    .build()
            }

        }

        private fun getUpdatedToken(): String? {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()

            val api = retrofit.create(RefreshTokenRepository::class.java)
            val res =  api.getAuthToken(
                RefreshTokenRequest(
                    userPreferences.readStringFromPreference("com.aasan.sample.refresh") ?: ""
                )
            )
            var authToken: String? = null
            val response = res.execute()
            if(response.isSuccessful){
                userPreferences.writeStringToPreference(
                    "com.aasan.sample.token",
                    "Bearer ${response.body()?.access?.token}"
                )
                userPreferences.writeStringToPreference(
                    "com.aasan.sample.refresh",
                    response.body()?.refresh?.token
                )
                authToken = "Bearer ${response.body()?.access?.token}"
            }


            return authToken
        }
    }


    class ResponseInterceptor(private val networkErrorListener: NetworkErrorListener? = null) :
        Interceptor {
        lateinit var okHttpClient: OkHttpClient
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            return kotlin.runCatching {
                var response = chain.proceed(request)
                response
            }.onSuccess {
                it
                /*if (it.code() == 401) {
                    okHttpClient.dispatcher().cancelAll()
                    networkErrorListener?.logout()
                }*/

            }.onFailure {
                if (it is UnknownHostException) {
                    networkErrorListener?.onDomainCouldNotReach()
                    Response.Builder().request(request).code(400).protocol(Protocol.HTTP_1_0)
                        .message("").body(
                            ResponseBody.create(
                                MediaType.parse("application/json"),
                                "{\"message\":\"${it.message}\"}"
                            )
                        ).build()
                } else {
                    networkErrorListener?.onApiNotFound()
                    Response.Builder().request(request).code(400).protocol(Protocol.HTTP_1_0)
                        .message("").body(
                            ResponseBody.create(
                                MediaType.parse("application/json"),
                                "{\"message\":\"${it.message}\"}"
                            )
                        ).build()
                }

            }.let {
                it.getOrDefault(
                    Response.Builder().request(request).code(400).protocol(Protocol.HTTP_1_0)
                        .message("").body(
                            ResponseBody.create(
                                MediaType.parse("application/json"),
                                "{\"message\":\"${it.exceptionOrNull()?.message}\"}"
                            )
                        ).build()
                )
            }
        }

    }
}