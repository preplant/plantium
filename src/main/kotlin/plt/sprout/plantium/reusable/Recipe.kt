package plt.sprout.plantium.reusable

import plt.sprout.plantium.reusable.Sprout.Companion.sprout
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

abstract class Recipe(val label: String) : Listener() {
    abstract val output: ItemStack

    abstract val shape: Array<String>

    abstract val ingredients: Map<Char, ItemStack>

    private val key: NamespacedKey = NamespacedKey(sprout.plugin, label)

    private var Player.discoveredRecipe: Boolean
        get() = this.hasDiscoveredRecipe(key)
        set(discover) {
            val discovered = this.hasDiscoveredRecipe(key)

            when {
                discover && !discovered -> this.discoverRecipe(key)
                !discover && discovered -> this.undiscoverRecipe(key)
            }
        }

    fun add() {
        sprout.planner.runTaskLater(sprout.plugin, Runnable {
            val recipe = ShapedRecipe(key, output).shape(*shape)

            ingredients.forEach { (key, item) ->
                recipe.setIngredient(key, item)
            }

            sprout.server.addRecipe(recipe)

            sprout.server.onlinePlayers.forEach { player ->
                player.discoveredRecipe = true
            }

            sprout.listen(this)
        }, 20L)
    }

    fun remove() {
        sprout.server.onlinePlayers.forEach { player ->
            player.discoveredRecipe = false
        }

        sprout.server.removeRecipe(key)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        player.discoveredRecipe = false
        player.discoveredRecipe = true
    }
}