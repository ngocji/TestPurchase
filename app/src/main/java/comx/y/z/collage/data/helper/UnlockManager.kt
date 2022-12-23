package comx.y.z.collage.data.helper

import com.jibase.pref.SharePref
import java.util.concurrent.TimeUnit

class UnlockManager(private val sharePref: SharePref) {
    // region unlock sticker
    private val TIME_UNLOCK_STICKER_PACK = TimeUnit.HOURS.toMillis(24)

    private fun getKeyUnlockStickerPack(id: String) = "unlock_stickers_$id"

    fun isStickerPackUnlocked(id: String): Boolean {
        return isUnlockedInternal(getKeyUnlockStickerPack(id), TIME_UNLOCK_STICKER_PACK)
    }

    fun unlockStickerPack(id: String) {
        sharePref.put(getKeyUnlockStickerPack(id), System.currentTimeMillis())
    }

    private val TIME_UNLOCK_MAGIC_DRAW = TimeUnit.HOURS.toMillis(24)
    fun isMagicDrawUnlocked(id: String): Boolean {
        return isUnlockedInternal("unlock_magic_draw_$id", TIME_UNLOCK_MAGIC_DRAW)
    }

    fun unlockMagicDraw(id: String) {
        sharePref.put(getKeyUnlockStickerPack(id), System.currentTimeMillis())
    }

    // endregion


    private fun isUnlockedInternal(keyLastTime: String, availableTimeUnlock: Long): Boolean {
        val lastTimeUnlock = sharePref.getLong(keyLastTime, 0L)
        return isPurchased() || System.currentTimeMillis() - lastTimeUnlock < availableTimeUnlock
    }

    private fun isPurchased(): Boolean {
        /*return BillingProcessor.isPurchased()*/ return true
    }
}