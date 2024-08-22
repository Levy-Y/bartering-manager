package org.piglinManager.piglinTrades;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.manager.ItemManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.piglinManager.piglinTrades.PiglinTrades.trades;
import static org.piglinManager.piglinTrades.PiglinTrades.is_enabled;
import static org.piglinManager.piglinTrades.PiglinTrades.log;

public class ReloadConfig {
    private final JavaPlugin plugin;

    public ReloadConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reloadPluginConfig() {
        plugin.saveDefaultConfig();

        trades.emptyTradeItemCache();
        plugin.reloadConfig();

        is_enabled = plugin.getConfig().getBoolean("enabled");
        log = plugin.getConfig().getBoolean("log");

        ConfigurationSection tradesSection = plugin.getConfig().getConfigurationSection("trades");

        if (tradesSection != null) {
            List<Map<?, ?>> vanillaTrades = tradesSection.getMapList("vanilla_trades");
            List<Map<?, ?>> oraxenTrades = tradesSection.getMapList("oraxen_trades");
            List<Map<?, ?>> mmoitemsTrades = tradesSection.getMapList("mmoitems_trades");

            if (vanillaTrades != null && !vanillaTrades.isEmpty()) {
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
                        } catch (IllegalArgumentException e) {
                            if (log) {
                                plugin.getLogger().warning(String.format("Couldn't find item with name %s", key));
                            }
                        }
                    }
                }
            } else if (log) {
                plugin.getLogger().info("No vanilla trades found!");
            }

            if (mmoitemsTrades != null && !mmoitemsTrades.isEmpty()) {
                try {
                    for (Map<?, ?> tradeMap : mmoitemsTrades) {
                        for (Map.Entry<?, ?> entry : tradeMap.entrySet()) {
                            String key = entry.getKey().toString();
                            Map<?, ?> trade = (Map<?, ?>) entry.getValue();

                            String itemID = (String) trade.get("item");
                            int chance = (int) trade.get("chance");

                            ItemStack mmoItem = MMOItems.plugin.getItems().getItem(MMOItems.plugin.getTypes().get(key), itemID);
                            if (mmoItem != null) {
                                trades.items.put(mmoItem, chance);
                            } else {
                                plugin.getLogger().warning(String.format("Couldn't build mmoitem with ID: %s and Category: %s", itemID, key));
                            }
                        }
                    }
                } catch (NoClassDefFoundError error) {
                    plugin.getLogger().warning("MMOItems cannot be found on the server. No items referenced from it in the config will be added to the trades.");
                }
            } else if (log) {
                plugin.getLogger().info("No mmoitems trades found!");
            }

            try {
                if (oraxenTrades != null && !oraxenTrades.isEmpty()) {
                    for (Map<?, ?> tradeMap : oraxenTrades) {
                        for (Map.Entry<?, ?> entry : tradeMap.entrySet()) {
                            String key = entry.getKey().toString();
                            Map<?, ?> trade = (Map<?, ?>) entry.getValue();

                            int amount = (int) trade.get("amount");
                            int chance = (int) trade.get("chance");

                            if (amount < 0) {
                                amount *= -1;
                            }

                            try {
                                ItemBuilder item = OraxenItems.getItemById(key);
                                if (item != null) {
                                    item.setAmount(amount);
                                    ItemStack builtItem = item.build();
                                    trades.items.put(builtItem, chance);
                                } else if (log) {
                                    plugin.getLogger().warning(String.format("Couldn't build oraxen item with item ID: %s", key));
                                }
                            } catch (NullPointerException e) {
                                plugin.getLogger().severe("An error occurred while trying to retrieve an item from oraxen!");
                            }
                        }
                    }
                } else if (log) {
                    plugin.getLogger().info("No oraxen trades found!");
                }
            } catch (NoClassDefFoundError e) {
                if (log) {
                    plugin.getLogger().warning("Oraxen cannot be found on the server. No items referenced from it in the config will be added to the trades.");
                }
            }

        } else if (log) {
            plugin.getLogger().info("Trades section not found in config!");
        }
    }

}