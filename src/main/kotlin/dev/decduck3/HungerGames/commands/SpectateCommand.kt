package dev.decduck3.HungerGames.commands

import dev.decduck3.HungerGames.HungerGames
import dev.decduck3.HungerGames.game.GameStatus
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class SpectateCommand : BukkitCommand("spectate") {
    override fun execute(
        sender: CommandSender, commandLabel: String, args: Array<out String>
    ): Boolean {
        val player = sender as Player
        val game = HungerGames.instance!!.playerGames[player.uniqueId.toString()]
        if (game?.status == GameStatus.Playing) {
            return false
        }
        player.gameMode = GameMode.SPECTATOR
        return true
    }
}