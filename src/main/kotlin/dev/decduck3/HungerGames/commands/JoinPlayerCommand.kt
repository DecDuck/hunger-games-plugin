package dev.decduck3.HungerGames.commands

import dev.decduck3.HungerGames.HungerGames
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

class JoinPlayerCommand : BukkitCommand("join") {
    override fun execute(
        sender: CommandSender, commandLabel: String, args: Array<out String>
    ): Boolean {
        val gameName = args[0]
        val game = HungerGames.instance!!.games.find { it.name == gameName }
        if (game == null) {
            sender.sendMessage(
                MiniMessage.miniMessage().deserialize("<gray>Unknown game <i><white>$gameName</white></i></gray>")
            )
            return true;
        }

        val playerName = args[1]
        val player = HungerGames.instance!!.server.getPlayer(playerName)
        if (player == null) {
            sender.sendMessage(
                MiniMessage.miniMessage().deserialize("<gray>Unknown player <i><white>$playerName</white></i></gray>")
            )
            return true;
        }

        // This sends a message in chat, so we don't need to do anything
        val error = game.join(player)
        if (error != null) {
            sender.sendMessage(
                MiniMessage.miniMessage().deserialize("<gray>$error</gray>")
            )

        }

        return true;
    }

    override fun tabComplete(
        sender: CommandSender, alias: String, args: Array<out String>
    ): List<String?> {
        if (args.size == 1) {
            return HungerGames.instance!!.games.map { it -> it.name }
        } else if (args.size == 2) {
            return HungerGames.instance!!.server.onlinePlayers.map { player -> player.name }
        }
        return listOf()
    }
}