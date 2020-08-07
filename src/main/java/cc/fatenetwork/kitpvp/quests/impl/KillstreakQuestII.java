package cc.fatenetwork.kitpvp.quests.impl;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.quests.Quest;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KillstreakQuestII extends Quest {
    private final KitPvP plugin;

    public KillstreakQuestII(String name, KitPvP plugin) {
        super(name, plugin);
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Kill Streak Quest II";
    }

    @Override
    public String getDescription() {
        return "Get a kill-streak of 5";
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.STONE_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(StringUtil.format("&dKill Streak I"));
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
        return null;
    }

    @Override
    public int getGoal() {
        return 5;
    }

    @Override
    public int getProgress(Profile profile) {
        return profile.getKillstreak();
    }

    @Override
    public void onKillPlayer(Player victim, Player attacker) {
        Profile profile = plugin.getProfileManager().getProfile(attacker.getUniqueId());
        if (profile.getKillstreak() >= getGoal()) {
            plugin.getQuestInterface().onQuestComplete(attacker, this);
        }
    }

    @Override
    public void onComplete(Player player, Quest quest) {
        if (quest == this) {
            Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
            profile.setActiveQuest(quest.getNextQuest());
        }
    }
}
