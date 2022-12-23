package comx.y.z.collage.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_entity")
data class CacheEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)