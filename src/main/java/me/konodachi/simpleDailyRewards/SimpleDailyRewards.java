package me.konodachi.simpleDailyRewards;

import me.konodachi.simpleDailyRewards.commands.ShowMenuCommand;
import me.konodachi.simpleDailyRewards.listeners.InventoryClickListener;
import me.konodachi.simpleDailyRewards.listeners.PlayerJoinListener;
import me.konodachi.simpleDailyRewards.listeners.PlayerQuitListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleDailyRewards extends JavaPlugin {

    @Override
    public void onEnable() {
        DatabaseHelper.prepareDatabase(getDataFolder().getPath() + "/database.db");
        // Register commands
        getCommand("daily").setExecutor(new ShowMenuCommand());
        // Register event listeners
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }
}
