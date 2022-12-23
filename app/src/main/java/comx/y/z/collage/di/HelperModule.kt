package comx.y.z.collage.di

import android.content.Context
import com.jibase.pref.SharePref
import comx.y.z.collage.data.helper.LocalFileManager
import comx.y.z.collage.data.helper.UnlockManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HelperModule {

    @Provides
    @Singleton
    fun provideFileManager(@ApplicationContext context: Context) = LocalFileManager(context)

    @Provides
    @Singleton
    fun provideUnlockManager(sharePref: SharePref) = UnlockManager(sharePref)
}