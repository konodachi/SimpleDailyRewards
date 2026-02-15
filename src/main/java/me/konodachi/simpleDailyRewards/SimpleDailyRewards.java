package me.konodachi.simpleDailyRewards;

import me.konodachi.simpleDailyRewards.commands.ShowMenuCommand;
import me.konodachi.simpleDailyRewards.listeners.InventoryClickListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleDailyRewards extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("daily").setExecutor(new ShowMenuCommand());
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
