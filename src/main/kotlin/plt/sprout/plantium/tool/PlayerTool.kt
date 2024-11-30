package plt.sprout.plantium.tool

import org.bukkit.Sound as BukkitSound
import plt.sprout.plantium.reusable.Sprout.Companion.sprout
import plt.sprout.plantium.tool.TextTool.deserialize
import java.time.Duration
import org.bukkit.entity.Player
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.title.Title

object PlayerTool {
    fun Player.messageDSR(
        message: String = "",
        prefixed: Boolean = false,
        lines: Boolean = false,
        blanks: Boolean = false
    ) {
        val beginning = when (prefixed) {
            true -> "${sprout.prefix} "
            false -> ""
        }

        val line = when (lines) {
            true -> TextTool.CHAT_DIVIDER
            false -> null
        }

        val blank = when (blanks) {
            true -> ""
            false -> null
        }

        val list = listOfNotNull(
            line,
            blank,
            "$beginning$message",
            blank,
            line,
        ).joinToString("\n")

        this.sendMessage(list.deserialize())
    }

    fun Player.actionDSR(text: String = "") {
        this.sendActionBar(text.deserialize())
    }

    fun Player.soundDSR(
        sound: BukkitSound,
        volume: Float = 1f,
        pitch: Float = 1f,
        emitter: Sound.Emitter = Sound.Emitter.self(),
        source: Sound.Source = Sound.Source.MASTER
    ) {
        this.playSound(Sound.sound(sound.key(), source, volume, pitch), emitter)
    }

    fun Player.titleDSR(
        title: String,
        subtitle: String,
        fadeIn: Long = 1000,
        onScreen: Long = 3000,
        fadeOut: Long = 1000
    ) {
        val times =
            Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(onScreen), Duration.ofMillis(fadeOut))

        this.showTitle(Title.title(title.deserialize(), subtitle.deserialize(), times))
    }
}