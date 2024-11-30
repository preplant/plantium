package plt.sprout.plantium.reusable

import plt.sprout.plantium.reusable.Sprout.Companion.sprout
import org.bukkit.event.Listener

abstract class Listener : Provider, Listener {
    override fun register() {
        sprout.listen(this)
    }
}