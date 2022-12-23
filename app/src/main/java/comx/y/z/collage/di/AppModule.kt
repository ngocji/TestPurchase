package comx.y.z.collage.di

import android.content.Context
import comx.y.z.collage.data.room.AppDataBase
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
    fun provideDataBase(@ApplicationContext context: Context) =
        AppDataBase.init(context, "blood.db", listOf())
}