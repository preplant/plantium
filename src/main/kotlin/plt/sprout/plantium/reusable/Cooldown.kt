package plt.sprout.plantium.reusable

import plt.sprout.plantium.reusable.Sprout.Companion.sprout
import plt.sprout.plantium.tool.PlayerTool.actionDSR
import plt.sprout.plantium.tool.PlayerTool.soundDSR
import kotlin.time.Duration
import kotlin.time.DurationUnit
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import org.bukkit.Sound
import org.bukkit.entity.Player

class Cooldown(val label: String, private val length: Duration) {
    private val cooldowns = ConcurrentHashMap<UUID, Duration>()

    val bypass: String
        get() {
            val replaced = label.replace(" ", ".")

            return sprout.getScopePermission("cooldown.$replaced.bypass")
        }

    operator fun set(player: Player, value: Boolean) {
        if (!player.hasPermission(bypass)) {
            cooldowns[player.uniqueId] = length

            sprout.planner.runTaskLaterAsynchronously(sprout.plugin, Runnable {
                cooldowns.remove(player.uniqueId)
            }, length.inWholeMilliseconds / 50)
        }
    }

    fun showReminder(player: Player) {
        cooldowns[player.uniqueId]?.let { duration ->
            val seconds = duration.toDouble(DurationUnit.SECONDS)
            val rounded = "%.2f".format(seconds)

            player.actionDSR("&fPlease wait &c$rounded seconds&f.")
            player.soundDSR(Sound.BLOCK_NOTE_BLOCK_BASS)
        }
    }

    fun isActive(player: Player): Boolean {
        return cooldowns[player.uniqueId] != null
    }
}