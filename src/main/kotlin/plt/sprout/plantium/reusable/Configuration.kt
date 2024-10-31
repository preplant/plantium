@file:Suppress("UNCHECKED_CAST")

package plt.sprout.plantium.reusable

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import java.io.File
import java.io.InputStreamReader
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import plt.sprout.plantium.reusable.Sprout.Companion.sprout

abstract class Configuration(val label: String, val path: String) {
    private val file: File by lazy {
        File(sprout.folder, path)
    }

    lateinit var configuration: YamlConfiguration

    private val fallback: FileConfiguration by lazy {
        sprout.plugin.getResource(path)?.let { configuration ->
            YamlConfiguration.loadConfiguration(InputStreamReader(configuration))
        } ?: YamlConfiguration()
    }

    open fun load() {
        if (!file.exists()) {
            sprout.plugin.saveResource(path, false)
        }

        configuration = YamlConfiguration.loadConfiguration(file)

        this::class.memberProperties.forEach { property ->
            try {
                property.isAccessible = true

                val mutable = property as? KMutableProperty1<Configuration, Any?>
                val setting = mutable?.getDelegate(this) as? Setting<*>

                setting?.let { setting ->
                    if (setting.path.isNullOrBlank()) {
                        setting.path = property.name
                            .replace("([a-z])([A-Z])".toRegex(), "$1.$2")
                            .lowercase()
                    }

                    val path = setting.path!!
                    val value = configuration.get(path) ?: fallback.get(path)

                    mutable.modify(value)
                }
            } catch (exception: Exception) {
                sprout.logger.severe("Error setting property ${property.name}: ${exception.message}!")
                exception.printStackTrace()
            } finally {
                property.isAccessible = false
            }
        }
    }

    open fun save() {
        configuration.save(file)
    }

    private fun KMutableProperty1<*, *>.modify(value: Any?) {
        val mutable = this as KMutableProperty1<Configuration, Any?>
        val setter = this.setter

        when (mutable.returnType.classifier) {
            Int::class -> setter.call(this@Configuration, (value as? Number)?.toInt())
            Boolean::class -> setter.call(this@Configuration, value as? Boolean)
            String::class -> setter.call(this@Configuration, value as? String)
            Double::class -> setter.call(this@Configuration, (value as? Number)?.toDouble())
            Float::class -> setter.call(this@Configuration, (value as? Number)?.toFloat())
            Long::class -> setter.call(this@Configuration, (value as? Number)?.toLong())
            List::class -> setter.call(this@Configuration, value as? List<*>)
            Map::class -> setter.call(this@Configuration, value as? Map<*, *>)
            else -> setter.call(this@Configuration, value)
        }
    }
}