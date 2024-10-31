@file:Suppress("UNUSED", "UNCHECKED_CAST")

package plt.sprout.plantium.reusable

import kotlin.reflect.KProperty

class Setting<T : Any>(var path: String? = null) {
    private var value: T? = null

    operator fun getValue(thiz: Any, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("Error returning uninitialised property ${property.name}.")
    }

    operator fun setValue(thiz: Any, property: KProperty<*>, updated: T) {
        value = updated
    }
}