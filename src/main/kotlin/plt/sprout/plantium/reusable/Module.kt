package plt.sprout.plantium.reusable

abstract class Module(val label: String) : Registrable {
    abstract val providers: List<Provider>

    override fun register() {
        providers.forEach { provider ->
            provider.register()
        }
    }

    override fun unregister() {
        providers.forEach { provider ->
            provider.unregister()
        }
    }
}