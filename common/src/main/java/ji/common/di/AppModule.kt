package ji.common.di

import com.jibase.di.DefaultPrefName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @DefaultPrefName
    @Provides
    @Singleton
    fun providePreferenceName() = "AppSharePref"
}