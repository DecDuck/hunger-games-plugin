package dev.decduck3.HungerGames.listeners

import dev.decduck3.HungerGames.HungerGames
import dev.decduck3.HungerGames.game.GameStatus
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val spawnLocation = HungerGames.instance!!.lobby!!.spawnLocation;
        event.player.teleport(spawnLocation)
        event.player.respawnLocation = spawnLocation
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.deathMessage = null
        val game = HungerGames.instance!!.playerGames[event.player.uniqueId.toString()]
        if (game?.status == GameStatus.Playing) {
            game.kill(event.player)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val spawnLocation = HungerGames.instance!!.lobby!!.spawnLocation;
        event.respawnLocation = spawnLocation;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerLeave(event: PlayerQuitEvent) {
        val game = HungerGames.instance!!.playerGames[event.player.uniqueId.toString()]
        if (game?.status == GameStatus.Playing) {
            game.kill(event.player)
        }
    }
}