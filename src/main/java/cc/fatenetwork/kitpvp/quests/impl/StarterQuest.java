package cc.fatenetwork.kitpvp.quests.impl;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.quests.Quest;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class StarterQuest extends Quest {
    private final KitPvP plugin;

    public StarterQuest(KitPvP plugin) {
        super("Starter Quest");
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Starter Quest";
    }

    @Override
    public String getDescription() {
        return "Kill a player!";
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(StringUtil.format("&9" + getName()));
        List<String> lore = new ArrayList<>();
        lore.add("&b" + getDescription());
        lore.add("&7Goal: &9" + getGoal());
        lore.add("&7Progress: &9" + 0 + "%");
        itemMeta.setLore(StringUtil.format(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getNextQuest() {
        return null;
    }

    @Override
    public int getGoal() {
        return 1;
    }

    @Override
    public int getProgress(Profile profile) {
        return 0;
    }

    @Override
    public void onKillPlayer(Player player) {
        plugin.getQuestInterface().onQuestComplete(player, this);
    }
}
