package com.mobivery.fct25.di

import android.content.Context
import com.mobivery.fct25.app.common.analytics.AnalyticsHelper
//import com.mobivery.fct25.data.network.SessionProvider
//import com.mobivery.fct25.session.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun analyticsHelper(@ApplicationContext context: Context): AnalyticsHelper =
        AnalyticsHelper(context)
}
