package plt.sprout.plantium.command

import plt.sprout.plantium.reusable.Sprout.Companion.sprout
import plt.sprout.plantium.tool.CommandTool.configureCustomArgument
import org.bukkit.entity.Player
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder
import dev.jorel.commandapi.arguments.StringArgument

class CustomPlayerArgument(
    optional: Boolean = false,
    permission: CommandPermission = CommandPermission.NONE,
    key: String = "target",
) : CustomArgument<Player, String>(
    StringArgument(key),
    { info ->
        sprout.server.getPlayer(info.input)
            ?: throw CustomArgumentException.fromMessageBuilder(
                MessageBuilder("That player is not online or does not exist")
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