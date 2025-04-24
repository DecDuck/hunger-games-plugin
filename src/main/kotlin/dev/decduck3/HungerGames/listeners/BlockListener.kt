package dev.decduck3.HungerGames.listeners

import dev.decduck3.HungerGames.HungerGames
import net.minecraft.world.level.block.TntBlock
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class BlockListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.block.type == Material.TNT) {
            event.block.type = Material.AIR
            val entity = event.block.world.spawnEntity(event.block.location, EntityType.TNT) as TNTPrimed
            entity.fuseTicks = 0
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (event.inventory.holder is Chest) {
            val chest = event.inventory.holder as Chest
            val game = HungerGames.instance!!.playerGames[event.player.uniqueId.toString()]
            if (game == null) return
            if (!event.inventory.isEmpty) return
            chest.block.type = Material.AIR
        }
    }
}