package dev.decduck3.HungerGames.commands

import com.mojang.brigadier.Command
import dev.decduck3.HungerGames.HungerGames
import dev.decduck3.HungerGames.game.Game
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import kotlin.text.get

class CreateGameCommand : BukkitCommand("create") {
    override fun execute(
        sender: CommandSender, commandLabel: String, args: Array<out String>
    ): Boolean {
        val arenaName = args[0];
        val arena = HungerGames.instance!!.arenas[arenaName]
        if (arena == null) {
            sender.sendMessage(
                MiniMessage.miniMessage()
                    .deserialize("<gray>Could not find arena <i><white>$arenaName</white></i></gray>")
            )
            return true;
        }

        val game = Game(arena, arenaName)
        HungerGames.instance!!.games.add(game)

        sender.sendMessage(
            MiniMessage.miniMessage().deserialize("<gray>Created game with arena <i><white>$arenaName</white></i></gray>")
        );

        return true;
    }

    override fun tabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): List<String?> {
        return HungerGames.instance!!.arenas.keys.toList()
    }


}