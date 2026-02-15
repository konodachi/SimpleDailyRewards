package me.konodachi.simpleDailyRewards.listeners;

import me.konodachi.simpleDailyRewards.DatabaseHelper;
import me.konodachi.simpleDailyRewards.SimpleDailyRewards;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {
    private SimpleDailyRewards plugin;

    public PlayerQuitListener(SimpleDailyRewards plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerID = event.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            DatabaseHelper.updateLoginData(playerID);
        });
    }

}
