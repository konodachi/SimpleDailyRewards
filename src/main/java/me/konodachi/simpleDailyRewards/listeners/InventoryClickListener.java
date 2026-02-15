package me.konodachi.simpleDailyRewards.listeners;

import me.konodachi.simpleDailyRewards.MenuHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isRightClick() || !event.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("Daily Rewards") || !(event.getWhoClicked() instanceof Player player)) return;
        event.setCancelled(true);

        String buttonLabel = event.getCurrentItem().getItemMeta().getDisplayName();
        Material buttonType = event.getCurrentItem().getType();

        if (buttonType == Material.RED_WOOL) MenuHelper.quitMenu(player);

    }
}
