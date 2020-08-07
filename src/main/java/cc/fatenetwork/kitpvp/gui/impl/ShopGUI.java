package cc.fatenetwork.kitpvp.gui.impl;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.logging.Logger;

public class ShopGUI implements Listener {
    private final KitPvP plugin;

    public ShopGUI(KitPvP plugin) {
        this.plugin = plugin;
    }

    public Inventory getShopGUI() {
        Inventory i = Bukkit.createInventory(null, 54, StringUtil.format("&a&lShop &7GUI"));
        Map<ItemStack, Integer> items = new HashMap<>();
        for (String s : plugin.getConfig().getConfigurationSection("shop").getKeys(false)) {
            FileConfiguration config = plugin.getConfig();
            String path = "shop." + s + ".";
            if (plugin.getConfig().contains(path + "potion")) {
                ItemStack itemStack = new ItemStack(Material.getMaterial(config.getString(path + "material")));
                PotionMeta itemMeta = (PotionMeta) itemStack.getItemMeta();
                itemMeta.setDisplayName(StringUtil.format(config.getString(path + "name")));
                itemMeta.setLore(StringUtil.format(config.getStringList(path + "lore")));
                itemMeta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(config.getString(path + "potion")), config.getInt(path + "duration"), config.getInt(path + "amplifier")), true);
                Potion potion = new Potion(1);
                potion.setType(PotionType.valueOf(config.getString(path + "type")));
                if (config.getBoolean(path + "splash")) {
                    potion.setSplash(true);
                }
                potion.apply(itemStack);
                itemStack.setItemMeta(itemMeta);
                items.put(itemStack, Integer.parseInt(s));
            } else {
                ItemStack itemStack = new ItemStack(Material.getMaterial(config.getString(path + "material")));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(StringUtil.format(config.getString(path + "name")));
                itemMeta.setLore(StringUtil.format(config.getStringList(path + "lore")));
                itemStack.setItemMeta(itemMeta);
                items.put(itemStack, Integer.parseInt(s));
            }
        }
        for (ItemStack item : items.keySet()) {
            i.setItem(items.get(item), item);
        }
        return i;
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Logger log = Bukkit.getLogger();
        Player player = (Player) event.getWhoClicked();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        if (event.getInventory() != null) {
            if (event.getClickedInventory() != null) {
                if (event.getClickedInventory().getName().equals(getShopGUI().getName())) {
                    log.info("[SHOP] Inventory found.");
                    event.setCancelled(true);
                    if (event.getCurrentItem() != null) {
                        log.info("[SHOP] Item found.");
                        ItemStack itemStack = event.getCurrentItem();
                        FileConfiguration config = plugin.getConfig();
                        log.info("[SHOP] slot = " + event.getRawSlot());
                        String path = "shop." + event.getRawSlot() + ".";
                        if (itemStack.getType() == Material.getMaterial(config.getString(path + "material"))) {
                            double coast = config.getDouble(path + "coast");
                            if (!(profile.getBalance() >= 0)) {
                                player.sendMessage(StringUtil.format("&cYou do not have enough money."));
                                return;
                            }
                            if (!(itemStack.getType() == Material.POTION)) {
                                profile.setBalance(profile.getBalance() - coast);
                                ItemStack item = new ItemStack(itemStack.getType());
                                player.getInventory().addItem(item);
                            } else {
                                ItemStack item = itemStack;
                                Potion potion =  new Potion(1);
                                PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
                                if (config.getBoolean(path + "splash")) {
                                    potion.setSplash(true);
                                    potionMeta.setLore(StringUtil.format(Arrays.asList("", "&7Splash to gain effect", "")));
                                } else {
                                    potionMeta.setLore(StringUtil.format(Arrays.asList("", "&7Drink to gain effect", "")));
                                }
                                item.setItemMeta(potionMeta);
                                potion.setType(PotionType.valueOf(config.getString(path + "type")));
                                potion.apply(item);
                                player.getInventory().addItem(item);
                            }
                        }
                    }
                }
            }
        }
    }
}
