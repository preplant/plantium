package plt.sprout.plantium.tool

import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object EventTool {
    fun PlayerInteractEvent.rmbCheck(item: ItemStack, continuation: (ItemStack) -> Unit) {
        val action = this.action

        if (this.hand == EquipmentSlot.HAND && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            this.item?.let { instance ->
                val hand = instance.clone().apply { amount = 1 }
                val comparison = item.clone().apply { amount = 1 }

                if (hand == comparison) {
                    continuation(instance)
                }
            }
        }
    }
}