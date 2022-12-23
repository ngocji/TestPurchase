package comx.y.z.collage.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import comx.y.z.collage.data.entity.CacheEntity

@Database(
    entities = [CacheEntity::class],
    version = 1
)
//@TypeConverters(DbConverter::class)
abstract class AppDataBase : RoomDatabase() {
    companion object {
        fun init(
            context: Context,
            dbName: String,
            migrations: List<Migration>,
            onInit: ((Context, SupportSQLiteDatabase) -> Unit)? = null
        ): AppDataBase {
            return Room.databaseBuilder(
                context,
                AppDataBase::class.java, dbName
            ).addMigrations(*migrations.toTypedArray())
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        onInit?.invoke(context, db)
                        super.onCreate(db)
                    }
                })
                .build()
        }
    }
}