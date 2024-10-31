package plt.sprout.plantium.tool

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType
import xyz.xenondevs.invui.item.ItemProvider
import com.google.common.collect.HashMultimap
import plt.sprout.plantium.tool.TextTool.deserialize
import plt.sprout.plantium.tool.TextTool.removeUnusedDecorations

class ItemTool : ItemProvider {
    constructor(material: Material, amount: Int = 1) {
        stack = ItemStack(material, amount)
    }

    constructor(item: ItemStack) {
        stack = item
    }

    companion object {
        val border = ItemTool(Material.BLACK_STAINED_GLASS_PANE)
            .setName("")
            .hideAll()
    }

    private val stack: ItemStack

    fun setName(name: String): ItemTool {
        stack.itemMeta = stack.itemMeta.apply {
            this.displayName(name.deserialize().removeUnusedDecorations(name))
        }

        return this
    }

    fun addLines(lines: List<String>, divider: Boolean = true): ItemTool {
        stack.itemMeta = stack.itemMeta.apply {
            val lore = this.lore()?.toMutableList() ?: mutableListOf()

            this.lore(lore.apply {
                if (divider) {
                    this.add(TextTool.LORE_DIVIDER.deserialize())
                }

                this.addAll(lines.map { line -> line.deserialize().removeUnusedDecorations(line) })
            })
        }

        return this
    }

    fun setUnbreakable(unbreakable: Boolean = true): ItemTool {
        stack.itemMeta = stack.itemMeta.apply {
            this.isUnbreakable = unbreakable
        }

        return this
    }

    fun addEnchantment(enchant: Enchantment, level: Int, restricted: Boolean = true): ItemTool {
        stack.itemMeta = stack.itemMeta.apply {
            this.addEnchant(enchant, level, restricted)
        }

        return this
    }

    fun addGlint(): ItemTool {
        stack.itemMeta = stack.itemMeta.apply {
            this.addEnchant(Enchantment.MENDING, 1, false)
        }

        return this
    }

    fun addModel(model: Int): ItemTool {
        stack.itemMeta = stack.itemMeta.apply {
            this.setCustomModelData(model)
        }

        return this
    }

    fun setPotionType(type: PotionType): ItemTool {
        stack.itemMeta = (stack.itemMeta as PotionMeta).apply {
            this.basePotionType = type
        }

        return this
    }

    fun hideAll(): ItemTool {
        stack.itemMeta = stack.itemMeta.apply {
            this.attributeModifiers = HashMultimap.create()

            this.addItemFlags(*ItemFlag.entries.toTypedArray())
        }

        return this
    }

    fun addGeneric(
        name: String,
        lines: List<String>,
        filler: Boolean = true,
        glint: Boolean = true
    ): ItemTool {
        this
            .setName(name)
            .addLines(lines, filler)
            .setUnbreakable()
            .hideAll()

        if (glint) {
            this.addGlint()
        }

        return this
    }

    override fun get(language: String?): ItemStack {
        return stack
    }
}