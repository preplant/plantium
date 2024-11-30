package plt.sprout.plantium.reusable

import plt.sprout.plantium.tool.CommandTool
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.CommandTree

abstract class Command(val label: String) : Provider {
    open val subcommands: List<Subcommand> = emptyList()

    open val aliases: List<String> = emptyList()

    open val permission: CommandPermission = CommandTool.getCommandPermission(label)

    protected val base: CommandTree = CommandTree(label)

    protected open val command: CommandTree = base

    override fun register() {
        // TODO: Implement automatic help usage.
        CommandAPI.unregister(label, true)

        command
            .withAliases(*aliases.toTypedArray())
            .withPermission(permission)
            .apply {
                subcommands.forEach { subcommand ->
                    this.then(subcommand.get())
                }
            }
            .register()
    }

    override fun unregister() {
        CommandAPI.unregister(label, true)
    }
}