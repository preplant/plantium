package plt.sprout.plantium.reusable

import plt.sprout.plantium.reusable.Sprout.Companion.sprout
import plt.sprout.plantium.tool.PlayerTool.actionDSR
import plt.sprout.plantium.tool.PlayerTool.soundDSR
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import org.bukkit.Sound
import org.bukkit.entity.Player

class Cooldown(val label: String, private val length: Duration) {
    companion object {
        private fun formatDuration(duration: Duration): String {
            val units = listOf(
                "day" to duration.inWholeDays,
                "hour" to duration.inWholeHours % 24,
                "minute" to duration.inWholeMinutes % 60,
                "second" to duration.inWholeSeconds % 60
            )

            val parts = units.filter { unit -> unit.second > 0 }
                .map { (label, value) ->
                    val extension = when (value == 1L) {
                        true -> ""
                        else -> "s"
                    }

                    "$value $label$extension"
                }

            return when (parts.size) {
                0 -> "less than a second"
                1 -> parts[0]
                2 -> parts.joinToString(" and ")
                else -> parts.dropLast(1).joinToString(", ") + ", and ${parts.last()}"
            }
        }
    }

    private val cooldowns = ConcurrentHashMap<UUID, Pair<Long, Duration>>()

    val bypass: String
        get() {
            val replaced = label.replace(" ", ".")

            return sprout.getScopePermission("cooldown.$replaced.bypass")
        }

    operator fun set(player: Player, value: Boolean) {
        if (!player.hasPermission(bypass)) {
            cooldowns[player.uniqueId] = System.currentTimeMillis() to length

            sprout.planner.runTaskLaterAsynchronously(sprout.plugin, Runnable {
                cooldowns.remove(player.uniqueId)
            }, length.inWholeMilliseconds / 50)
        }
    }

    fun showReminder(player: Player) {
        cooldowns[player.uniqueId]?.let { (start, duration) ->
            val elapsed = (System.currentTimeMillis() - start).milliseconds
            val formatted = formatDuration(duration - elapsed)

            player.actionDSR("&fPlease wait &6$formatted seconds&f.")
            player.soundDSR(Sound.BLOCK_NOTE_BLOCK_BASS)
        }
    }

    fun isActive(player: Player): Boolean {
        return cooldowns[player.uniqueId] != null
    }

    fun executeCooldown(player: Player, execute: () -> Unit) {
        if (!isActive(player)) {
            execute()

            this[player] = true
        } else {
            showReminder(player)
        }
    }
}