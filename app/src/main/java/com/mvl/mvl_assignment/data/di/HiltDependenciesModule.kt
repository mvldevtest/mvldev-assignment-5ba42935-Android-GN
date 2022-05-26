package com.mvl.mvl_assignment.data.di

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.mvl.mvl_assignment.BuildConfig
import com.mvl.mvl_assignment.contract.IRemoteDataSource
import com.mvl.mvl_assignment.contract.IRepository
import com.mvl.mvl_assignment.contract.IWebService
import com.mvl.mvl_assignment.domain.GetListAddressByYear
import com.mvl.mvl_assignment.domain.SendLocationRequest
import com.mvl.mvl_assignment.network.FHPRepository
import com.mvl.mvl_assignment.network.FiveHundredPixelsAPI
import com.mvl.mvl_assignment.network.NetworkDataSource
import com.mvl.mvl_assignment.network.RetrofitWebService
import com.mvl.mvl_assignment.ui.base.BaseActivity
import com.mvl.mvl_assignment.domain.GetSingleLocationRequest
import com.mvl.mvl_assignment.local.MapDao
import com.mvl.mvl_assignment.local.RunningDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


/**
 * Hilt Module class that builds our dependency graph
 * @author Prasan
 * @since 1.0
 */
@InstallIn(SingletonComponent::class)
@Module
object HiltDependenciesModule {




    @Provides
    fun provideBaseActivity(activity: Activity): BaseActivity {
        check(activity is BaseActivity) { "Every Activity is expected to extend BaseActivity" }
        return activity as BaseActivity
    }

    @Singleton
    @Provides
    fun provideMapDao(@ApplicationContext appContext: Context) : MapDao {
        return RunningDatabase.getInstance(appContext).mapDao
    }

    @Singleton
    @Provides
    fun provideMapDBRepository(mapDao: MapDao) = RoomDBRepository(mapDao)




    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences? {
        return context.getSharedPreferences("mandap_pref", Context.MODE_PRIVATE)
    }


    /**
     * Returns the [HttpLoggingInterceptor] instance with logging level set to body
     * @since 1.0.0
     */
    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    /**
     * Provides an [OkHttpClient]
     * @param loggingInterceptor [HttpLoggingInterceptor] instance
     * @since 1.0.0
     */
    @Provides
    fun provideOKHttpClient(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient().apply {
        OkHttpClient.Builder().run {

            addInterceptor(loggingInterceptor)
                    .followRedirects(true)
                    .followSslRedirects(true)
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                                .build()
                        chain.proceed(newRequest)
                    }
                     build()
        }
    }

    /**
     * Returns a [MoshiConverterFactory] instance
     * @since 1.0.0
     */
    @Provides
    fun provideMoshiConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    /**
     * Returns an instance of the [FiveHundredPixelsAPI] interface for the retrofit class
     * @return [FiveHundredPixelsAPI] impl
     * @since 1.0.0
     */
    @Provides
    fun provideRetrofitInstance(
           client: OkHttpClient,
           moshiConverterFactory: MoshiConverterFactory
    ): FiveHundredPixelsAPI =
            Retrofit.Builder().run {
                baseUrl(BuildConfig.ServerURL)
               addConverterFactory(moshiConverterFactory)
               client(client)
               build()
            }.run {
                create(FiveHundredPixelsAPI::class.java)
            }


    /**
     * Returns a [IWebService] impl
     * @param retrofitClient [FiveHundredPixelsAPI] retrofit interface
     * @since 1.0.0
     */
    @Provides
    fun providesRetrofitService(retrofitClient: FiveHundredPixelsAPI): IWebService =
            RetrofitWebService(retrofitClient)

    /**
     * Returns a [IRemoteDataSource] impl
     * @param webService [IWebService] instance
     * @since 1.0.0
     */
    @Provides
    fun providesNetworkDataSource(webService: IWebService): IRemoteDataSource =
            NetworkDataSource(webService)

    /**
     * Returns a singleton [IRepository] implementation
     * @param remoteDataSource [IRemoteDataSource] implementation
     * @since 1.0.0
     */
    @Provides
    fun provideRepository(remoteDataSource: IRemoteDataSource): IRepository =
            FHPRepository(remoteDataSource)


    @Provides
    @Singleton
    fun provideAddLocation(repository: IRepository): SendLocationRequest = SendLocationRequest(repository)

    @Provides
    @Singleton
    fun provideGetLocation(repository: IRepository): GetSingleLocationRequest = GetSingleLocationRequest(repository)

    @Provides
    @Singleton
    fun ProvideGetSingleLocation(repository: IRepository): GetListAddressByYear = GetListAddressByYear(repository)


}