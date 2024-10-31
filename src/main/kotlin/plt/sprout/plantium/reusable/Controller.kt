package plt.sprout.plantium.reusable

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.potion.PotionType
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.impl.controlitem.PageItem
import plt.sprout.plantium.reusable.Sprout.Companion.sprout
import plt.sprout.plantium.tool.ItemTool
import plt.sprout.plantium.tool.PlayerTool.soundDSR

object Controller {
    object BackItem : PageItem(false) {
        override fun getItemProvider(gui: PagedGui<*>): ItemProvider {
            val current = gui.currentPage + 1
            val maximum = gui.pageAmount

            if (gui.hasPreviousPage()) {
                return ItemTool(Material.TIPPED_ARROW)
                    .setPotionType(PotionType.LONG_LEAPING)
                    .setName("${sprout.colour}Previous Page")
                    .addLines(listOf(
                        "&7• &fTransition: &e$current &7/ &e$maximum &7» &6${current + 1} &7/ &6$maximum",
                        "",
                        "${sprout.colour}Click &7to show!"
                    ))
                    .addGlint()
                    .hideAll()
            } else {
                return ItemTool.border
            }
        }

        override fun handleClick(click: ClickType, player: Player, event: InventoryClickEvent) {
            val gui = gui as PagedGui

            if (gui.hasPreviousPage()) {
                super.handleClick(click, player, event)

                player.soundDSR(Sound.ITEM_BOOK_PAGE_TURN)
            }
        }
    }

    object NextItem : PageItem(true) {
        override fun getItemProvider(gui: PagedGui<*>): ItemProvider {
            val current = gui.currentPage + 1
            val maximum = gui.pageAmount

            if (gui.hasNextPage()) {
                return ItemTool(Material.TIPPED_ARROW)
                    .setPotionType(PotionType.LONG_FIRE_RESISTANCE)
                    .setName("${sprout.colour}Next Page")
                    .addLines(listOf(
                        "&7• &fTransition: &6$current &7/ &6$maximum &7» &e${current - 1} &7/ &e$maximum",
                        "",
                        "${sprout.colour}Click &7to show!"
                    ))
                    .addGlint()
                    .hideAll()
            } else {
                return ItemTool.border
            }
        }

        override fun handleClick(click: ClickType, player: Player, event: InventoryClickEvent) {
            val gui = gui as PagedGui

            if (gui.hasNextPage()) {
                super.handleClick(click, player, event)

                player.soundDSR(Sound.ITEM_BOOK_PAGE_TURN)
            }
        }
    }
}