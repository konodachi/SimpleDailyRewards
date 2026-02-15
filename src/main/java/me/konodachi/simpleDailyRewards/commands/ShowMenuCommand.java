package me.konodachi.simpleDailyRewards.commands;

import me.konodachi.simpleDailyRewards.MenuHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class ShowMenuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String s, String @NonNull [] strings) {

        if (!(commandSender instanceof Player player)) return false;

        MenuHelper.showMenu(player, 3, 2, false);

        return true;
    }
}
