package plt.sprout.plantium.reusable

import org.jetbrains.exposed.sql.Database as Exposed
import plt.sprout.plantium.reusable.Sprout.Companion.sprout
import kotlin.time.Duration
import org.bukkit.scheduler.BukkitTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

abstract class Database(val label: String, val path: String, private val autosave: Duration) {
    abstract val tables: List<Table>

    private var task: BukkitTask? = null

    fun initialise() {
        Exposed.connect("jdbc:sqlite:${sprout.folder}/$path", "org.sqlite.JDBC")

        transaction {
            SchemaUtils.createMissingTablesAndColumns(*tables.toTypedArray())

            loadData()
        }

        startTask()
    }

    fun finalise() {
        stopTask()

        transaction {
            saveData()
        }

        sprout.coroutineJob.cancel()
    }

    protected abstract fun loadData()

    protected abstract fun saveData()

    private fun startTask() {
        val ticks = autosave.inWholeMilliseconds / 50

        task = sprout.planner.runTaskTimerAsynchronously(sprout.plugin, Runnable {
            sprout.launch {
                newSuspendedTransaction(Dispatchers.IO) {
                    saveData()
                }
            }
        }, ticks, ticks)
    }

    private fun stopTask() {
        task?.cancel()
    }
}