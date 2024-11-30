package plt.sprout.plantium.reusable

import java.io.File
import java.util.logging.Logger
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class Sprout(val label: String) : CoroutineScope {
    companion object {
        lateinit var sprout: Sprout
    }

    abstract val colour: String

    abstract val prefix: String

    open val database: Database? = null

    open val configurations: List<Configuration> = emptyList()

    abstract val modules: List<Module>

    lateinit var plugin: JavaPlugin
        private set

    val server: Server by lazy {
        plugin.server
    }

    val logger: Logger by lazy {
        plugin.logger
    }

    val planner: BukkitScheduler by lazy {
        server.scheduler
    }

    val folder: File by lazy {
        plugin.dataFolder
    }

    val coroutineJob = Job()

    override val coroutineContext = Dispatchers.IO + coroutineJob

    open fun load(instance: JavaPlugin, commandAPIConfiguration: CommandAPIBukkitConfig) {
        plugin = instance
        sprout = this

        CommandAPI.onLoad(commandAPIConfiguration)
    }

    open fun enable() {
        CommandAPI.onEnable()

        database?.initialise()

        configurations.forEach { configuration ->
            configuration.load()
        }

        modules.forEach { module ->
            module.register()
        }
    }

    open fun disable() {
        modules.forEach { module ->
            module.unregister()
        }

        database?.finalise()

        CommandAPI.onDisable()
    }

    fun listen(listener: Listener) {
        server.pluginManager.registerEvents(listener, plugin)
    }

    fun getScopePermission(permission: String): String {
        return "$label.$permission"
    }
}