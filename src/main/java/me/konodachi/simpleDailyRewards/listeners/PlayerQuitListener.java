package me.konodachi.simpleDailyRewards.listeners;

import me.konodachi.simpleDailyRewards.DatabaseHelper;
import me.konodachi.simpleDailyRewards.SimpleDailyRewards;
import me.konodachi.simpleDailyRewards.data.LoginData;
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
        LoginData loginData = DatabaseHelper.dumpPlayerData(playerID);
        if (loginData == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            DatabaseHelper.updateLoginData(loginData);
        });
    }

}
