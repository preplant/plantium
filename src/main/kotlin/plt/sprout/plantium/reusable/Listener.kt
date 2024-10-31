package plt.sprout.plantium.reusable

import org.bukkit.event.Listener
import plt.sprout.plantium.reusable.Sprout.Companion.sprout

abstract class Listener : Provider, Listener {
    override fun register() {
        sprout.listen(this)
    }
}