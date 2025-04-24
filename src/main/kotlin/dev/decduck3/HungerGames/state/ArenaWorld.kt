package dev.decduck3.HungerGames.state

import dev.decduck3.HungerGames.HungerGames
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.WorldCreator

class ArenaWorld(
    var worldName: String? = null,
    var spawnpoints: List<Location>? = null,
    var chests: Map<Material, List<Location>>? = null
) {

    companion object {
        fun import(name: String): ArenaWorld {
            val world = WorldCreator(name).createWorld()
            if (world == null) {
                throw RuntimeException("Could not initialise arena: $name")
            }
            HungerGames.instance!!.logger.info("Loading arena: $name")

            val chunkRange = HungerGames.instance!!.config.get("chunkRange") as Int

            val chests: MutableMap<Material, MutableList<Location>> = mutableMapOf()
            val spawnpoints: MutableList<Location> = mutableListOf()

            for (x in -chunkRange..chunkRange) {
                HungerGames.instance!!.logger.info("Loading: ${(x + chunkRange) / (chunkRange * 0.02)}%")
                for (z in -chunkRange..chunkRange) {
                    if (!world.isChunkGenerated(x, z)) continue
                    val chunk = world.getChunkAt(x, z)
                    for (chunkX in 0..15) {
                        for (chunkZ in 0..15) {
                            for (chunkY in -64..319) {
                                val block = chunk.getBlock(chunkX, chunkY, chunkZ)
                                if (block.type == Material.AIR) continue

                                if (block.type == Material.CHEST) {
                                    val underneathMaterial = chunk.getBlock(chunkX, chunkY - 1, chunkZ).type;
                                    chests.putIfAbsent(underneathMaterial, mutableListOf())
                                    chests[underneathMaterial]!!.add(block.location)
                                    continue
                                }

                                if (block.type == Material.GOLD_BLOCK) {
                                    spawnpoints.add(block.location)
                                    continue
                                }
                            }
                        }
                    }
                }
            }

            return ArenaWorld(world.name, spawnpoints, chests)
        }
    }
}