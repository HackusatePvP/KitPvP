package cc.fatenetwork.kitpvp.gui.impl;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SettingsGUI implements Listener {
    private final KitPvP plugin;

    public SettingsGUI(KitPvP plugin) {
        this.plugin = plugin;
    }

    public Inventory getSettingsGUI(Profile profile) {
        Inventory i = Bukkit.createInventory(null, 9, StringUtil.format("&4&l" + profile.getName() + " &7Settings"));
        i.addItem(getSetting("scoreboard", profile), getSetting("chat", profile));
        return i;
    }

    public ItemStack getSetting(String setting, Profile profile) {
        if (setting.equals("scoreboard")) {
            ItemStack itemStack = new ItemStack(Material.PAINTING);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(StringUtil.format("&7* &4Scoreboard"));
            List<String> lore = new ArrayList<>();
            lore.add("&7&m----------------------------");
            lore.add("&7&oEnables/Disables your scoreboard");
            lore.add("");
            if (profile.isScoreboard()) {
                lore.add("&7Scoreboard: &a" + profile.isScoreboard());
            } else {
                lore.add("&7Scoreboard: &c" + profile.isScoreboard());
            }
            lore.add("&7&m----------------------------");
            itemMeta.setLore(StringUtil.format(lore));
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        if (setting.equals("chat")) {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(StringUtil.format("&7* &4Chat"));
            List<String> lore = new ArrayList<>();
            lore.add("&7&m----------------------------");
            lore.add("&7&oEnables/Disables your chat");
            lore.add("");
            if (profile.isScoreboard()) {
                lore.add("&7Chat: &a" + profile.isChat());
            } else {
                lore.add("&7Chat: &c" + profile.isChat());
            }
            lore.add("&7&m----------------------------");
            itemMeta.setLore(StringUtil.format(lore));
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        return null;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != null) {
            Player player = (Player) event.getWhoClicked();
            Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
            if (event.getInventory() != null) {
                if (event.getClickedInventory() != null) {
                    if (event.getClickedInventory().getName().equals(getSettingsGUI(profile).getName())) {
                        event.setCancelled(true);
                        if (event.getCurrentItem() != null) {

                            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(getSetting("scoreboard", profile).getItemMeta().getDisplayName())) {
                                if (profile.isScoreboard()) {
                                    player.sendMessage(StringUtil.format("&cYou have disabled your scoreboard."));
                                    profile.setScoreboard(false);
                                    player.closeInventory();
                                    player.openInventory(getSettingsGUI(profile));
                                    return;
                                }
                                player.sendMessage(StringUtil.format("&aYou have enabled your scoreboard."));
                                profile.setScoreboard(true);
                                player.closeInventory();
                                player.openInventory(getSettingsGUI(profile));
                            }

                            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(getSetting("chat", profile).getItemMeta().getDisplayName())) {
                                if (profile.isChat()) {
                                    player.sendMessage(StringUtil.format("&cYou have disabled your chat."));
                                    profile.setChat(false);
                                    player.closeInventory();
                                    player.openInventory(getSettingsGUI(profile));
                                    return;
                                }
                                player.sendMessage(StringUtil.format("&aYou have enabled your chat."));
                                profile.setChat(true);
                                player.closeInventory();
                                player.openInventory(getSettingsGUI(profile));
                            }
                        }
                    }
                }
            }
        }
    }
}
