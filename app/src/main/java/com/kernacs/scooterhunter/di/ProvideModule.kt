package com.kernacs.scooterhunter.di

import android.content.Context
import android.util.Log
import com.kernacs.scooterhunter.BuildConfig
import com.kernacs.scooterhunter.data.source.local.ScootersDatabase
import com.kernacs.scooterhunter.newtwork.ScooterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProvideModule {

    @Provides
    fun provideHttpClient() = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })

            engine {
                connectTimeout = TIME_OUT
                socketTimeout = TIME_OUT
            }
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }

            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header("secret-key", BuildConfig.SECRET_KEY)
        }

        expectSuccess = false
        HttpResponseValidator {
            handleResponseException { exception ->
                throw exception
            }
        }
    }

    @Provides
    fun scooterApi(client: HttpClient): ScooterApi = ScooterApi(client)

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        ScootersDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideDao(db: ScootersDatabase) = db.scootersDao()

    companion object {
        private const val TIME_OUT = 60_000
    }
}