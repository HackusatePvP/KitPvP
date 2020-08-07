package cc.fatenetwork.kitpvp.gui.impl;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.quests.Quest;
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
import java.util.Arrays;
import java.util.List;

public class QuestGUI implements Listener {
    private final KitPvP plugin;

    public QuestGUI(KitPvP plugin) {
        this.plugin = plugin;
    }

    public Inventory getQuestGUI(Profile profile) {
        List<ItemStack> items = new ArrayList<>();
        for (String s : profile.getQuests()) {
            Quest quest = Quest.getByName(s);
            if (quest != null) {
                items.add(quest.getItem());
            }
        }
        Inventory i = Bukkit.createInventory(null, 9, StringUtil.format("&9&lQuests"));
        if (items.size() != 0) {

            for (ItemStack itemStack : items) {
                i.addItem(itemStack);
            }
            return i;
        }
        ItemStack itemStack = new ItemStack(Material.REDSTONE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(StringUtil.format("&b&lERROR: "));
        itemMeta.setLore(StringUtil.format(Arrays.asList("", "&7You have no available quests", "")));
        itemStack.setItemMeta(itemMeta);
        i.addItem(itemStack);
        return i;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != null) {
            Player player = (Player) event.getWhoClicked();
            Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
            if (event.getInventory() != null) {
                if (event.getClickedInventory() != null) {
                    if (event.getClickedInventory().getName().equals(StringUtil.format("&9&lQuests"))) {
                        event.setCancelled(true);
                        if (event.getCurrentItem() != null) {
                            for (String s : profile.getQuests()) {
                                Quest quest = Quest.getByName(s);
                                if (quest != null) {
                                    if (quest.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(event.getCurrentItem().getItemMeta().getDisplayName())) {
                                        if (profile.getActiveQuest().equalsIgnoreCase(quest.getName())) {
                                            player.sendMessage(StringUtil.format("&cThis is already your active quest."));
                                            return;
                                        }
                                        profile.setActiveQuest(s);
                                        player.sendMessage(StringUtil.format("&7You have selected &9" + quest.getName() + " &7as your active quest."));
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
