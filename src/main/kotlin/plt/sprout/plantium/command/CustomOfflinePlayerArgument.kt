package plt.sprout.plantium.command

import plt.sprout.plantium.reusable.Sprout.Companion.sprout
import plt.sprout.plantium.tool.CommandTool.configureCustomArgument
import org.bukkit.OfflinePlayer
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder
import dev.jorel.commandapi.arguments.StringArgument

class CustomOfflinePlayerArgument(
    optional: Boolean = false,
    permission: CommandPermission = CommandPermission.NONE,
    key: String = "target",
) : CustomArgument<OfflinePlayer, String>(
    StringArgument(key),
    { info ->
        sprout.server.getOfflinePlayer(info.input)
            .takeIf { player -> player.firstPlayed > 0L }
            ?: throw CustomArgumentException.fromMessageBuilder(
                MessageBuilder("That player does not exist")
            )
    }
) {
    init {
        this.replaceSuggestions(ArgumentSuggestions.strings { _ ->
            sprout.server.onlinePlayers
                .map { online -> online.name }
                .toTypedArray()
        })

        this.configureCustomArgument(optional, permission)
    }
}