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
        super("Starter Quest", plugin);
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
        lore.add("&7&m-------------------------");
        lore.add("&b" + getDescription());
        lore.add("");
        lore.add("&7Goal: &9" + getGoal());
        lore.add("&7Progress: &9" + 0 + "%");
        lore.add("&7&m-------------------------");
        itemMeta.setLore(StringUtil.format(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getNextQuest() {
        return Quest.KILLSTREAK_I_QUEST.getName();
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
    public void onKillPlayer(Player player, Player attacker) {
        plugin.getQuestInterface().onQuestComplete(attacker, this);
    }

    @Override
    public void onComplete(Player player, Quest quest) {
        if (quest == this) {
            Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
            profile.getQuests().add(Quest.CLAN_QUEST.getName());
            profile.setActiveQuest(quest.getNextQuest());
        }
    }
}
