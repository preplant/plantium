package plt.sprout.plantium.tool

import plt.sprout.plantium.reusable.Sprout.Companion.sprout
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.Argument

object CommandTool {
    fun getCommandPermission(permission: String): CommandPermission {
        return CommandPermission.fromString("${sprout.label}.command.$permission")
    }

    fun CommandPermission.getOthersPermission(): CommandPermission {
        return CommandPermission.fromString("${this.permission}.others")
    }

    fun Argument<*>.configureCustomArgument(optional: Boolean, permission: CommandPermission) {
        if (optional) {
            this.isOptional = true
        }

        this.withPermission(permission)
    }
}