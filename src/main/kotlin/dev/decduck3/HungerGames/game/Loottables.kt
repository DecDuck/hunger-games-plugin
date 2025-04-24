package dev.decduck3.HungerGames.game

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

object Loottables {
    data class Lootobject(val item: Material, val min: Int, val max: Int)

    val random = Random(System.currentTimeMillis());

    val loottables: List<Pair<Float, Lootobject>> = listOf(
        // foods
        0.1f to Lootobject(Material.BAKED_POTATO, 1, 3),
        0.1f to Lootobject(Material.COOKED_COD, 1, 2),

        // materials
        0.1f to Lootobject(Material.COBBLESTONE, 2, 5),
        0.1f to Lootobject(Material.OAK_LOG, 1, 3),
        0.05f to Lootobject(Material.IRON_ORE, 1, 3),
        0.05f to Lootobject(Material.DIAMOND, 1, 1),
        0.07f to Lootobject(Material.SAND, 3, 13),
        0.03f to Lootobject(Material.GUNPOWDER, 1, 3),

        // weapons
        0.03f to Lootobject(Material.STONE_AXE, 1, 1),
        0.03f to Lootobject(Material.BOW, 1, 1),
        0.1f to Lootobject(Material.ARROW, 5, 15),
        0.004f to Lootobject(Material.IRON_AXE, 1, 1),
        0.001f to Lootobject(Material.DIAMOND_AXE, 1, 1),

        // armor
        0.004f to Lootobject(Material.IRON_CHESTPLATE, 1, 1),
        0.0001f to Lootobject(Material.DIAMOND_CHESTPLATE, 1, 1),
    )

    val totalWeight = loottables.map { it.first }.sum()
    const val TARGET_PER_CHEST = 10;

    fun generateChest(size: Int, distanceScalar: Double): Array<ItemStack> {

        val target = ((size / 27) * TARGET_PER_CHEST * distanceScalar.pow(1)).roundToInt()

        val inventory = mutableListOf<ItemStack>()
        for (i in 0 until target) {
            var itemRoll = random.nextFloat() * totalWeight;
            inventory.add(ItemStack(Material.PAPER, 1))

            for ((amount, material) in loottables) {
                itemRoll -= amount
                if (itemRoll > 0) continue

                val amount =
                    if (material.min == material.max) material.min else random.nextInt(material.min, material.max)

                inventory[i] = ItemStack(material.item, amount)
                break
            }
        }

        repeat(size - target) { inventory.add(ItemStack(Material.AIR)) }

        return inventory.shuffled().toTypedArray()
    }
}