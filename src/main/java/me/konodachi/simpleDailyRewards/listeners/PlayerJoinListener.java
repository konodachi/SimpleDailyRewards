package me.konodachi.simpleDailyRewards.listeners;

import me.konodachi.simpleDailyRewards.DatabaseHelper;
import me.konodachi.simpleDailyRewards.data.LoginData;
import me.konodachi.simpleDailyRewards.SimpleDailyRewards;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.util.UUID;

public class PlayerJoinListener implements Listener {
    SimpleDailyRewards plugin;

    public PlayerJoinListener(SimpleDailyRewards plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerID = event.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
            DatabaseHelper.fetchLoginData(playerID);
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (plugin.getServer().getPlayer(playerID) == null) {
                    DatabaseHelper.dumpPlayerData(playerID);
                    return;
                }
                LoginData data = DatabaseHelper.getData(playerID);
                if (data == null) return;
                if (data.getLastClaim().isBefore(LocalDate.now().minusDays(1))) DatabaseHelper.resetStreak(playerID);
                if (data.getLastClaim().isEqual(LocalDate.now().minusDays(1))) data.setAlreadyClaimed(false);
                if (!data.alreadyClaimed()) event.getPlayer().sendMessage("Don't forget to claim your daily reward with /daily");
            });
        });
    }
}
