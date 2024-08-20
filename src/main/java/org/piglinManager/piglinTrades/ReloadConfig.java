package org.piglinManager.piglinTrades;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.piglinManager.piglinTrades.PiglinTrades.trades;
import static org.piglinManager.piglinTrades.PiglinTrades.is_enabled;
import static org.piglinManager.piglinTrades.PiglinTrades.log;

public class ReloadConfig {
    private final JavaPlugin plugin;

    public ReloadConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reloadPluginConfig() {
        plugin.reloadConfig();

        is_enabled = plugin.getConfig().getBoolean("enabled");
        log = plugin.getConfig().getBoolean("log");

        trades.items = new HashMap<ItemStack, Integer>();

        ConfigurationSection tradesSection = plugin.getConfig().getConfigurationSection("trades");

        if (tradesSection != null) {
            List<Map<?, ?>> vanillaTrades = tradesSection.getMapList("vanilla_trades");
            List<Map<?, ?>> oraxenTrades = tradesSection.getMapList("oraxen_trades");

            if (!vanillaTrades.isEmpty()) {
                for (Map<?, ?> tradeMap : vanillaTrades) {
                    for (Map.Entry<?, ?> entry : tradeMap.entrySet()) {
                        String key = entry.getKey().toString();
                        Map<?, ?> trade = (Map<?, ?>) entry.getValue();

                        int amount = (int) trade.get("amount");
                        int chance = (int) trade.get("chance");

                        if (amount < 0) {
                            amount *= -1;
                        }

                        try {
                            trades.items.put(new ItemStack(Material.valueOf(key.toUpperCase()), amount), chance);
                        } catch (java.lang.IllegalArgumentException e) {
                            if (log) {
                                plugin.getLogger().warning(String.format("Couldn't find item with name %s", key));
                            }
                            return;
                        }
                    }
                }
            } else if (log) {
                plugin.getLogger().info("No vanilla trades found!");
            }

            try {
                if (!oraxenTrades.isEmpty()) {
                    for (Map<?, ?> tradeMap : oraxenTrades) {
                        for (Map.Entry<?, ?> entry : tradeMap.entrySet()) {
                            String key = entry.getKey().toString();
                            Map<?, ?> trade = (Map<?, ?>) entry.getValue();

                            int amount = (int) trade.get("amount");
                            int chance = (int) trade.get("chance");

                            if (amount < 0) {
                                amount *= -1;
                            }

                            ItemBuilder item = OraxenItems.getItemById(key);
                            if (item != null) {
                                item.setAmount(amount);
                                ItemStack builtItem = item.build();
                                trades.items.put(builtItem, chance);
                            } else if (log) {
                                plugin.getLogger().warning(String.format("Couldn't build oraxen item with item ID: %s", key));
                            }
                        }
                    }
                } else if (log) {
                    plugin.getLogger().info("No oraxen trades found!");
                }
            } catch (java.lang.NoClassDefFoundError e) {
                if (log) {
                    plugin.getLogger().warning("Oraxen cannot be found on the server. No items referenced from it in the config will be added to the trades.");
                }
            }

        }
    }
}