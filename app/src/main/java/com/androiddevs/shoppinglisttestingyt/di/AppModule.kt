package com.androiddevs.shoppinglisttestingyt.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDao
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDatabase
import com.androiddevs.shoppinglisttestingyt.data.remote.PixabayApi
import com.androiddevs.shoppinglisttestingyt.other.Constants.Base_URL
import com.androiddevs.shoppinglisttestingyt.other.Constants.DB_Name
import com.androiddevs.shoppinglisttestingyt.repositories.DefaultShoppingRepository
import com.androiddevs.shoppinglisttestingyt.repositories.ShoppingRepository
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
 @Singleton
 @Provides
 fun provideShoppingDatabase(@ApplicationContext context: Context)=
     Room.databaseBuilder(context,ShoppingDatabase::class.java,DB_Name)
         .build()

    @Singleton
    @Provides
    fun provideShoppingDao(database: ShoppingDatabase)=
        database.getShoppingDao()

   @Singleton
   @Provides
    fun providePixabayApi():PixabayApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Base_URL)
            .build()
            .create(PixabayApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
      dao: ShoppingDao,
      api: PixabayApi
    )=DefaultShoppingRepository(dao,api) as ShoppingRepository

    @Provides
    fun provideGlideInstance(@ApplicationContext context: Context) =
        Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
                      )

}