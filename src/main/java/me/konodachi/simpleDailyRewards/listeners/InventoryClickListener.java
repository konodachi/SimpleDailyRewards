package me.konodachi.simpleDailyRewards.listeners;

import me.konodachi.simpleDailyRewards.DatabaseHelper;
import me.konodachi.simpleDailyRewards.MenuHelper;
import me.konodachi.simpleDailyRewards.SimpleDailyRewards;
import me.konodachi.simpleDailyRewards.data.LoginData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class InventoryClickListener implements Listener {
    SimpleDailyRewards plugin;

    public InventoryClickListener(SimpleDailyRewards plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isRightClick() || !event.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("Daily Rewards") || !(event.getWhoClicked() instanceof Player player)) return;
        event.setCancelled(true);

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;
        if (currentItem.getItemMeta() == null) return;
        if (currentItem.getItemMeta().getLore() == null) return;
        String buttonLabel = currentItem.getItemMeta().getDisplayName();
        List<String> buttonLore = currentItem.getItemMeta().getLore();
        Material buttonType = currentItem.getType();



        if (buttonType == Material.RED_WOOL) MenuHelper.quitMenu(player);

        if (!(buttonType == Material.NETHER_STAR)) return;

        if (!buttonLore.getFirst().equalsIgnoreCase("Click to claim rewards for today")) return;

        giveRewards(player.getUniqueId(), buttonLabel);
    }

    public void giveRewards(UUID uuid, String buttonLabel){
        LoginData loginData = DatabaseHelper.getData(uuid);
        if (loginData == null) {plugin.getLogger().severe("Check database integrity!");return;}

        List<ItemStack> rewards = plugin.getLoot().get(buttonLabel);

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        for (ItemStack reward : rewards) {
            if (player.getInventory().firstEmpty() == -1){
                player.getWorld().dropItemNaturally(player.getLocation(), reward);
            }
            player.getInventory().addItem(reward);
        }

        loginData.setDays(loginData.getDays() + 1);
        loginData.setLastClaim(LocalDate.now());
        loginData.setAlreadyClaimed(true);
    }

}
