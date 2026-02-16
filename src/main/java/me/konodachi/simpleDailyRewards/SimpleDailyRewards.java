package me.konodachi.simpleDailyRewards;

import me.konodachi.simpleDailyRewards.commands.ReloadConfig;
import me.konodachi.simpleDailyRewards.commands.ShowMenuCommand;
import me.konodachi.simpleDailyRewards.listeners.InventoryClickListener;
import me.konodachi.simpleDailyRewards.listeners.PlayerJoinListener;
import me.konodachi.simpleDailyRewards.listeners.PlayerQuitListener;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.util.*;

public final class SimpleDailyRewards extends JavaPlugin {
    Map<String, List<ItemStack>> loot;
    Configuration config;

    @Override
    public void onEnable() {
        DatabaseHelper.prepareDatabase(getDataFolder().getPath() + "/database.db");

        loadConfig();
        loadLoot();

        getCommand("daily").setExecutor(new ShowMenuCommand());
        getCommand("reloadConfig").setExecutor(new ReloadConfig(this));

        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }

    public void loadConfig() {
        saveDefaultConfig();
        config = getConfig();

        if (config.getConfigurationSection("loot-table") == null || config.getConfigurationSection("loot-table").getKeys(false).isEmpty()) fallbackToDefaultConfig();

    }

    public void fallbackToDefaultConfig() {
        getLogger().warning("Invalid configuration! Falling back to defaults.");
        config = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("config.yml")));
    }

    public void loadLoot() {
        ConfigurationSection lootTable =  config.getConfigurationSection("loot-table");
        loot = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            String dayKey = String.format("Day %d", i);
            if (!lootTable.contains(dayKey)){ loadDefaultLoot(); return; }
            ConfigurationSection day = lootTable.getConfigurationSection(dayKey);
            if (!day.contains("items")) { loadDefaultLoot(); return; }

            List<Map<?, ?>> itemsData = day.getMapList("items");
            List<ItemStack> items = new ArrayList<>();

            prepareItemList(itemsData, items);

            loot.put(dayKey, items);
        }
    }

    public void prepareItemList(List<Map<?, ?>> itemsData, List<ItemStack> items) {
        for (Map<?, ?> item : itemsData) {
            if (!item.containsKey("material")){
                loadDefaultLoot();
                return;
            }
            if (item.get("material") == null){
                loadDefaultLoot();
                return;
            }
            String material = (String)  item.get("material");
            if (material.isEmpty()){
                loadDefaultLoot();
                return;
            }

            int amount;
            if (!item.containsKey("amount")) amount = 1;
            else if (item.get("amount") == null || !(item.get("amount") instanceof Integer)){
                loadDefaultLoot();
                return;
            }
            else amount = (int) item.get("amount");

            Material materialEnum = Material.getMaterial(material.toUpperCase());

            if (materialEnum == null) {
                loadDefaultLoot();
                return;
            }

            ItemStack itemStack = new ItemStack(materialEnum, amount);

            items.add(itemStack);
        }
    }

    public void loadDefaultLoot() {
        fallbackToDefaultConfig();
        loadLoot();
    }

    public Map<String, List<ItemStack>> getLoot() {
        return loot;
    }
}
