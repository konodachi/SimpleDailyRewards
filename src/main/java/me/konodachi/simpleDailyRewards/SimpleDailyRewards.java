package me.konodachi.simpleDailyRewards;

import me.konodachi.simpleDailyRewards.commands.ShowMenuCommand;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.event.MenuListener;

public final class SimpleDailyRewards extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("daily").setExecutor(new ShowMenuCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
