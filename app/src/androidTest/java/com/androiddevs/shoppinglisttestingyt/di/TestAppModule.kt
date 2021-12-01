package com.androiddevs.shoppinglisttestingyt.di

import android.content.Context
import androidx.room.Room
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {
    @Named("test_db")
    @Provides
    fun provideInAppMemory(@ApplicationContext context:Context)=
        Room.inMemoryDatabaseBuilder(context,ShoppingDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    }
