package dev.decduck3.HungerGames.game

import dev.decduck3.HungerGames.HungerGames
import dev.decduck3.HungerGames.state.ArenaWorld
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.*
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import java.io.IOException
import java.lang.Math.pow
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


class Game(val arenaWorld: ArenaWorld, val name: String) {

    var status: GameStatus = GameStatus.Lobby;
    val maxPlayers: Int = arenaWorld.spawnpoints!!.size;
    val players: MutableList<GamePlayerState> = mutableListOf()
    val world: World
    val size = 300.0;
    val tmpWorldName = UUID.randomUUID().toString()

    @Throws(IOException::class)
    fun copyFolder(src: Path, dest: Path) {
        Files.walk(src).use { stream ->
            stream.forEach { source -> Files.copy(source, dest.resolve(src.relativize(source))) }
        }
    }

    init {
        val source = Paths.get("./", arenaWorld.worldName)
        val target = Paths.get("./", tmpWorldName)

        copyFolder(source, target)

        val uidDat = Paths.get(target.toString(), "uid.dat").toFile()
        uidDat.delete()

        world = WorldCreator(tmpWorldName).createWorld()!!
        world.time = 1000
    }


    private fun broadcast(message: Component) {
        for (player in players) {
            player.player.sendMessage(message)
        }
    }

    /**
     * Joins player to game
     * @return String if failed, otherwise null
     */
    fun join(player: Player): String? {
        if (players.size == maxPlayers) {
            return "Too many players";
        }
        if (players.find { it.player == player } != null) {
            return "Player already joined.";
        }
        val existing = HungerGames.instance!!.playerGames[player.uniqueId.toString()]
        if (existing != null) {
            return "Player already part of a game.";
        }

        players += GamePlayerState(player)
        HungerGames.instance!!.playerGames[player.uniqueId.toString()] = this;
        broadcast(
            MiniMessage.miniMessage()
                .deserialize("<gray>Player <b><yellow>${player.name}</yellow></b> joined the game</gray>")
        )

        return null
    }

    fun cleanup() {
        val spawnLocation = HungerGames.instance!!.lobby!!.spawnLocation;

        for (player in players) {
            disconnect(player.player)

            player.player.teleport(spawnLocation);
        }

        HungerGames.instance!!.games.remove(this);
        HungerGames.instance!!.pendingTmpWorlds.add(tmpWorldName)
    }

    fun win(player: GamePlayerState) {
        broadcast(
            MiniMessage.miniMessage().deserialize("<i><b><gold>${player.player.name}</gold></b> is the victor!</i>")
        )

        cleanup()
    }

    fun checkWin() {
        val remaining = players.filter { !it.dead }
        if (remaining.size == 1) {
            val winner = remaining.first()
            win(winner)
        }
    }

    /**
     * Marks a player as dead
     */
    fun kill(player: Player) {
        if (players.find { it.player == player }!!.dead) {
            return;
        };

        // Set death and move to lobby
        players.find { it.player == player }!!.dead = true;

        // Broadcast death
        for (player in players) {
            player.player.playSound(player.player.location, Sound.PARTICLE_SOUL_ESCAPE, 10f, 0.1f)
        }
        broadcast(
            MiniMessage.miniMessage().deserialize(
                "<i><gray>${player.name} has been eliminated</gray></i>"
            )
        )

        checkWin()
    }

    /**
     * Player disconnects, but we shouldn't treat it as a dead for visual purposes
     */
    fun disconnect(player: OfflinePlayer) {
        HungerGames.instance!!.playerGames.remove(player.uniqueId.toString())
    }

    fun spawnIn() {
        val spawnpointsShuffled = arenaWorld.spawnpoints!!.shuffled()

        for (i in 0 until players.size) {
            val spawnpoint = spawnpointsShuffled[i].add(0.0, 1.0, 0.0).toCenterLocation();
            spawnpoint.world = world

            val player = players[i].player;

            player.health = 20.0;
            player.foodLevel = 20;
            player.saturation = 3.0f
            player.clearActivePotionEffects()
            player.gameMode = GameMode.SURVIVAL;
            player.inventory.clear()

            world.worldBorder.size = size;

            world.setGameRule(GameRule.DO_TILE_DROPS, true);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.time = 1000;

            player.teleport(spawnpoint)
        }
    }

    fun populateChests() {
        val spawn = world.spawnLocation
        for (chest in arenaWorld.chests!!.values.flatten()) {
            val block = world.getBlockAt(chest).state as Chest;
            val inventory = block.inventory;
            inventory.clear()

            val distance = sqrt((chest.x - spawn.x).pow(2) + (chest.z - spawn.z).pow(2))
            val distanceScalar = (distance / size).coerceIn(0.0, 1.0);

            inventory.contents = Loottables.generateChest(inventory.size, distanceScalar)
        }
    }


    /**
     * Starts the game
     * @return String if failed, otherwise null
     */
    fun start(): String? {
        if (players.size < 2) {
            return "Not enough players"
        }

        spawnIn()
        populateChests()
        status = GameStatus.Playing

        broadcast(MiniMessage.miniMessage().deserialize("<b><white>LET THE GAMES BEGIN</white></b>"))

        return null
    }
}
