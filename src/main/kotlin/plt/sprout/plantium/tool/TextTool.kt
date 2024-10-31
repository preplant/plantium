package plt.sprout.plantium.tool

import java.util.concurrent.ConcurrentHashMap
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

object TextTool {
    const val CHAT_DIVIDER = "&7&m                                                            "
    const val LORE_DIVIDER = "&8&m                                              "

    fun Component.serialize(section: Boolean = false): String {
        return when (section) {
            true -> LegacyComponentSerializer.legacySection().serialize(this)
            else -> LegacyComponentSerializer.legacyAmpersand().serialize(this)
        }
    }

    fun String.deserialize(section: Boolean = false): Component {
        return when (section) {
            true -> LegacyComponentSerializer.legacySection().deserialize(this)
            else -> LegacyComponentSerializer.legacyAmpersand().deserialize(this)
        }
    }

    fun Component.hoverDSR(list: List<String>): Component {
        var component = this

        list.forEach { line ->
            component = component.hoverEvent(HoverEvent.showText(line.deserialize()))
        }

        return component
    }

    fun Component.removeDecorations(): Component {
        var component = this

        TextDecoration.entries.forEach { decoration ->
            component = component.decoration(decoration, false)
        }

        return component
    }

    fun Component.removeUnusedDecorations(original: String): Component {
        var component = this

        val map = mapOf(
            'k' to TextDecoration.OBFUSCATED,
            'l' to TextDecoration.BOLD,
            'm' to TextDecoration.STRIKETHROUGH,
            'n' to TextDecoration.UNDERLINED,
            'o' to TextDecoration.ITALIC
        )

        val settings = ConcurrentHashMap<TextDecoration, Boolean>()

        TextDecoration.entries.forEach { decoration ->
            settings[decoration] = false
        }

        map.forEach { (code, decoration) ->
            if (original.contains("&$code")) {
                settings[decoration] = true
            }
        }

        settings.forEach { (decoration, isPresent) ->
            component = component.decoration(decoration, isPresent)
        }

        return component
    }
}