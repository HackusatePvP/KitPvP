package cc.fatenetwork.kitpvp.gui.impl;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.clans.Clan;
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
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ClanGUI implements Listener {
    private final KitPvP plugin;

    public ClanGUI(KitPvP plugin) {
        this.plugin = plugin;
    }

    public Inventory getClanSettings(Clan clan) {
        Inventory i = Bukkit.createInventory(null, 9, StringUtil.format("&c&l" + clan.getName() + "'s &8Settings"));
        i.addItem(getSetting(clan, "open"));

        return i;
    }

    public ItemStack getSetting(Clan clan, String setting) {
        if (setting.equalsIgnoreCase("open")) {
            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM);
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setOwner("Player");
            if (clan.isOpen()) {
                skullMeta.setDisplayName(StringUtil.format("&a&lOpen"));
                List<String> lore = new ArrayList<>();
                lore.add("&7&m----------------------------------");
                lore.add("&7Your clan is &aopen.");
                lore.add("");
                lore.add("&a&lOPEN &8- &7Anyone can join the clan without an invitation.");
                lore.add("&c&lCLOSED &8 - &7Players would need an invite to join the clan.");
                lore.add("");
                lore.add("&7&oClick to update the setting.");
                lore.add("&7&m----------------------------------");
                skullMeta.setLore(StringUtil.format(lore));
            } else {
                skullMeta.setDisplayName(StringUtil.format("&c&lClose"));
                List<String> lore = new ArrayList<>();
                lore.add("&7&m----------------------------------");
                lore.add("&7Your clan is &cclosed.");
                lore.add("");
                lore.add("&a&lOPEN &8- &7Anyone can join the clan without an invitation.");
                lore.add("&c&lCLOSED &8 - &7Players would need an invite to join the clan.");
                lore.add("");
                lore.add("&7&oClick to update the setting.");
                lore.add("&7&m----------------------------------");
                skullMeta.setLore(StringUtil.format(lore));
            }
            itemStack.setItemMeta(skullMeta);
            return itemStack;
        }
        if (setting.equalsIgnoreCase("team-damage")) {
            ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (clan.isTeamDamage()) {
                List<String> lore = new ArrayList<>();
                lore.add("&7&m----------------------------------");
                lore.add("&7Your clan has &6Team-Damage &aenabled.");
                lore.add("");
                lore.add("&a&lENABLED &8- &7Clan members can attack eachother");
                lore.add("&c&lDISABLED &8- &7Clan members cannot damage eachother");
                lore.add("");
                lore.add("&7&oClick to update the setting");
                lore.add("&7&m----------------------------------");
                itemMeta.setLore(StringUtil.format(lore));
            } else {
                List<String> lore = new ArrayList<>();
                lore.add("&7&m----------------------------------");
                lore.add("&7Your clan has &6Team-Damage &cdisabled..");
                lore.add("");
                lore.add("&a&lENABLED &8- &7Clan members can attack eachother");
                lore.add("&c&lDISABLED &8- &7Clan members cannot damage eachother");
                lore.add("");
                lore.add("&7&oClick to update the setting");
                lore.add("&7&m----------------------------------");
                itemMeta.setLore(StringUtil.format(lore));
            }
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
            if (profile.isClan()) {
                Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                if (clan != null) {
                    if (event.getInventory() != null) {
                        if (event.getClickedInventory() != null) {
                            if (event.getClickedInventory().getName().equals(getClanSettings(clan).getName())) {
                                event.setCancelled(true);
                                if (event.getCurrentItem() != null) {
                                    ItemStack itemStack = event.getCurrentItem();
                                    if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(getSetting(clan,"open").getItemMeta().getDisplayName())) {
                                        if (!clan.getElites().contains(player.getName())) {
                                            player.sendMessage(StringUtil.format("&cYou must be an elite member or higher to change this setting."));
                                            return;
                                        }
                                        if (clan.isOpen()) {
                                            clan.setOpen(false);
                                            player.sendMessage(StringUtil.format("&cYou have closed the clan."));
                                            return;
                                        }
                                        player.sendMessage(StringUtil.format("&aThe clan is now opened."));
                                        clan.setOpen(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
