package dev.decduck3.HungerGames

import dev.decduck3.HungerGames.commands.CreateGameCommand
import dev.decduck3.HungerGames.commands.JoinPlayerCommand
import dev.decduck3.HungerGames.commands.SpectateCommand
import dev.decduck3.HungerGames.commands.StartGameCommand
import dev.decduck3.HungerGames.game.Game
import dev.decduck3.HungerGames.listeners.BlockListener
import dev.decduck3.HungerGames.listeners.PlayerListener
import dev.decduck3.HungerGames.state.ArenaWorld
import dev.decduck3.HungerGames.state.Serializer
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Paths


@Suppress("UnstableApiUsage")
class HungerGames : JavaPlugin() {
    companion object {
        var instance: HungerGames? = null
    }

    val arenas: MutableMap<String, ArenaWorld> = mutableMapOf()
    val games: MutableList<Game> = mutableListOf()
    val playerGames: MutableMap<String, Game> = mutableMapOf()
    val pendingTmpWorlds: MutableList<String> = mutableListOf()
    var lobby: World? = null

    override fun onEnable() {
        instance = this
        saveDefaultConfig();

        val configuredArenas = config.get("arenas") as List<String>;
        for (arenaName in configuredArenas) {
            val arenaFilename = "$arenaName.obj";
            val arenaData = Serializer.deserialize<ArenaWorld>(arenaFilename, ArenaWorld::class.java)
            if (arenaData == null) {
                // We need to import this world
                val importedData = ArenaWorld.import(arenaName)
                Serializer.serialize(arenaFilename, importedData)
                arenas.put(arenaName, importedData)
            } else {
                arenas.put(arenaName, arenaData);
            }
        }

        val lobbyName = config.get("lobby") as String
        lobby = WorldCreator(lobbyName).createWorld()

        val tntRecipe = ShapelessRecipe(NamespacedKey(this, "tnt"), ItemStack(Material.TNT, 1))
        tntRecipe.addIngredient(Material.GUNPOWDER)
        tntRecipe.addIngredient(Material.SAND)
        server.addRecipe(tntRecipe)

        server.pluginManager.registerEvents(PlayerListener(), this)
        server.pluginManager.registerEvents(BlockListener(), this)

        server.commandMap.register(name.lowercase(), CreateGameCommand())
        server.commandMap.register(name.lowercase(), JoinPlayerCommand())
        server.commandMap.register(name.lowercase(), StartGameCommand())
        server.commandMap.register(name.lowercase(), SpectateCommand())

        server.scheduler.scheduleSyncRepeatingTask(this, {
            cleanupTmpWorlds()
        }, 20, 20 * 60 * 5)
    }

    override fun onDisable() {
        // Plugin shutdown logic
        cleanupTmpWorlds(force = true)
    }

    fun cleanupTmpWorlds(force: Boolean = false) {
        while (pendingTmpWorlds.isNotEmpty()) {
            val tmpWorld = pendingTmpWorlds.first()
            val unloaded = server.unloadWorld(tmpWorld, false)
            if (!unloaded && force) throw RuntimeException("Failed to unload worlds: $pendingTmpWorlds")
            if (unloaded) {
                val worldDir = Paths.get("./", tmpWorld).toFile()
                worldDir.deleteRecursively()
                pendingTmpWorlds.remove(tmpWorld)
                logger.info { "Removed $worldDir" }
            }
        }
    }

}


