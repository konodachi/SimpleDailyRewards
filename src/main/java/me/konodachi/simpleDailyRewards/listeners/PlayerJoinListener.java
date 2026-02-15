package me.konodachi.simpleDailyRewards.listeners;

import me.konodachi.simpleDailyRewards.DatabaseHelper;
import me.konodachi.simpleDailyRewards.LoginData;
import me.konodachi.simpleDailyRewards.SimpleDailyRewards;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlayerJoinListener implements Listener {
    SimpleDailyRewards plugin;

    public PlayerJoinListener(SimpleDailyRewards plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
            DatabaseHelper.fetchLoginData(playerId);
            Bukkit.getScheduler().runTask(plugin, () -> {
                LoginData data = DatabaseHelper.getData(playerId);
                if (data.alreadyClaimed()) return;
                else event.getPlayer().sendMessage("Don't forget to claim your daily reward with /daily");
            });
        });
    }
}
