package me.konodachi.simpleDailyRewards;

import me.konodachi.simpleDailyRewards.data.LoginData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuHelper {

    private MenuHelper(){};

    public static void showMenu(LoginData loginData) {
        List<ItemStack> buttons = new ArrayList<>();
        int weeklyMultiplier = 5;

        Player player = Bukkit.getPlayer(loginData.getPlayerID());
        int days = loginData.getDays();
        int weeks = loginData.getWeeks();
        boolean alreadyClaimed = loginData.alreadyClaimed();


        ItemStack quitButton = makeButton("Close Menu", Material.RED_WOOL);
        buttons.add(quitButton);

        for (int i = 1; i <= 7; i++){
            List<String> lore = new ArrayList<>();

            if (i < days) lore.add("This reward has already been claimed.");
            else if (i == days && !alreadyClaimed) lore.add("Click to claim rewards for today");
            else if (i == days) lore.add("This reward has already been claimed.");
            else lore.add("Reward not available yet.");

            buttons.add(makeButton("Day " + (i), Material.NETHER_STAR, lore));
        }

        List<String> weeklyLore = List.of("Daily rewards are increased %" + weeklyMultiplier + " for every consecutive week",
                                    "Consecutive weeks: " + weeks);
        ItemStack weeklyMultiplierIndicator = makeButton("Weekly Multiplier: " + ((float) 1 + (float) weeklyMultiplier / 100), Material.EMERALD, weeklyLore);
        buttons.add(weeklyMultiplierIndicator);

        Inventory menu = Bukkit.createInventory(player, 9, "Daily Rewards");

        for (ItemStack button : buttons){
            menu.addItem(button);
        }

        player.openInventory(menu);
    }

    public static ItemStack makeButton(String label, Material material){
        ItemStack button = new ItemStack(material);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(label);
        button.setItemMeta(meta);

        return button;
    }

    public static ItemStack makeButton(String label, Material material, List<String> lore){
        ItemStack button = makeButton(label, material);
        ItemMeta meta = button.getItemMeta();
        meta.setLore(lore);
        button.setItemMeta(meta);

        return button;
    }

    public static void quitMenu(Player player){
        player.closeInventory();
    }
}
