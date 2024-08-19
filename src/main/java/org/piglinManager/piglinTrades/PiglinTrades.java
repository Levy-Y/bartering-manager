package org.piglinManager.piglinTrades;

import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Piglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import io.th0rgal.oraxen.items.ItemBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class PiglinTrades extends JavaPlugin implements Listener {
    public boolean is_enabled = true;
    public boolean log = true;

    TradeItems trades = new TradeItems();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadPluginConfig();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPiglinBarter(EntityDropItemEvent event) {
        if (!is_enabled) {
            return;
        }

        Entity entity = event.getEntity();
        if (entity instanceof Piglin piglin) {
            event.getItemDrop().setItemStack(Objects.requireNonNull(TradeItems.selectItem(trades.items)));
        }
    }

    public void reloadPluginConfig() {
        reloadConfig();

        is_enabled = getConfig().getBoolean("enabled");
        log = getConfig().getBoolean("log");

        trades.items = new HashMap<ItemStack, Integer>();

        ConfigurationSection tradesSection = getConfig().getConfigurationSection("trades");

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
                                getLogger().warning(String.format("Couldn't find item with name %s", key));
                            }
                            return;
                        }
                    }
                }
            } else if (log) {
                getLogger().info("No vanilla trades found!");
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
                                getLogger().warning(String.format("Couldn't build oraxen item with item ID: %s", key));
                            }
                        }
                    }
                } else if (log) {
                    getLogger().info("No oraxen trades found!");
                }
            } catch (java.lang.NoClassDefFoundError e) {
                if (log) {
                    getLogger().warning("Oraxen cannot be found on the server. No items referenced from it in the config will be added to the trades.");
                }
            }

        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
