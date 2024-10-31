package plt.sprout.plantium.reusable

import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import plt.sprout.plantium.tool.CommandTool

abstract class Subcommand(val parent: Command, val sublabel: String) {
    val label: String = parent.label

    open val permission: CommandPermission = CommandTool.getCommandPermission("$label.$sublabel")

    protected val base: MultiLiteralArgument = MultiLiteralArgument("subcommand", sublabel)

    protected open val subcommand: Argument<String> = base

    fun get(): Argument<String> {
        return subcommand
            .withPermission(permission)
    }
}