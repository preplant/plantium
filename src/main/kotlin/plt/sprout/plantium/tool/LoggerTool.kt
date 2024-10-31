package plt.sprout.plantium.tool

import java.util.logging.Logger

object LoggerTool {
    private val colours = linkedMapOf(
        "&0" to "#000000",   // Black
        "&1" to "#0000AA",   // Dark Blue
        "&2" to "#00AA00",   // Dark Green
        "&3" to "#00AAAA",   // Dark Aqua
        "&4" to "#AA0000",   // Dark Red
        "&5" to "#AA00AA",   // Dark Purple
        "&6" to "#FFAA00",   // Gold
        "&7" to "#AAAAAA",   // Gray
        "&8" to "#555555",   // Dark Gray
        "&9" to "#5555FF",   // Blue
        "&a" to "#55FF55",   // Green
        "&b" to "#55FFFF",   // Aqua
        "&c" to "#FF5555",   // Red
        "&d" to "#FF55FF",   // Light Purple
        "&e" to "#FFFF55",   // Yellow
        "&f" to "#FFFFFF",   // White
        "&k" to "\u001B[0m", // Obfuscated
        "&l" to "\u001B[1m", // Bold
        "&m" to "\u001B[9m", // Strikethrough
        "&n" to "\u001B[4m", // Underline
        "&o" to "\u001B[3m", // Italic
        "&r" to "\u001B[0m"  // Reset
    )

    private val pattern = "&#([0-9a-fA-F]{6})".toPattern()

    fun Logger.send(input: String, prefixed: Boolean = false) {
        var clone = input

        val matcher = pattern.matcher(clone)
        val buffer = StringBuffer()

        while (matcher.find()) {
            val ansi = "#${matcher.group(1)}".toAnsi()

            matcher.appendReplacement(buffer, ansi)
        }

        matcher.appendTail(buffer)

        clone = buffer.toString()

        colours.forEach { (key, value) ->
            val ansi = when (key) {
                "&r" -> value
                else -> value.toAnsi()
            }

            clone = clone.replace(key, ansi)
        }

        val result = "$clone\u001B[0m"

        when (prefixed) {
            true -> this.info(result)
            else -> println(result)
        }
    }

    private fun String.toAnsi(): String {
        val red = Integer.valueOf(this.substring(1, 3), 16)
        val green = Integer.valueOf(this.substring(3, 5), 16)
        val blue = Integer.valueOf(this.substring(5, 7), 16)

        return "\u001B[38;2;${red};${green};${blue}m"
    }
}