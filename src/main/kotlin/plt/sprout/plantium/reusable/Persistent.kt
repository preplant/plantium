package plt.sprout.plantium.reusable

/**
 * Marks a property as intended for persistence in a [Database] or [Configuration].
 *
 * This annotation is for developer reference only and does not affect runtime behaviour.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class Persistent