package dev.decduck3.HungerGames.commands

import dev.decduck3.HungerGames.HungerGames
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

class StartGameCommand : BukkitCommand("start") {
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

        val errorMessage = game.start()
        if (errorMessage != null) {
            sender.sendMessage(
                MiniMessage.miniMessage().deserialize("<gray>$errorMessage</gray>")
            )
            return true;
        }

        return true;
    }

    override fun tabComplete(
        sender: CommandSender, alias: String, args: Array<out String>
    ): List<String?> {
        return HungerGames.instance!!.games.map { it.name }
    }
}