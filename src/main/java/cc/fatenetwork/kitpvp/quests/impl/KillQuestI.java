package cc.fatenetwork.kitpvp.quests.impl;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.quests.Quest;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class KillQuestI extends Quest {
    private final KitPvP plugin;


    public KillQuestI(String name, KitPvP plugin) {
        super(name, plugin);
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Kill Quest I";
    }

    @Override
    public String getDescription() {
        return "Kill 10 players";
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner("Skeleton");
        skullMeta.setDisplayName(StringUtil.format("&4" + getName()));
        List<String> lore = new ArrayList<>();
        lore.add("&7&m-------------------------");
        lore.add("&b" + getDescription());
        lore.add("");
        lore.add("&7Goal: &9" + getGoal());
        lore.add("&7Progress: &9" + 0 + "%");
        lore.add("&7&m-------------------------");
        skullMeta.setLore(StringUtil.format(lore));
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    @Override
    public String getNextQuest() {
        return null;
    }

    @Override
    public int getGoal() {
        return 10;
    }

    @Override
    public int getProgress(Profile profile) {
        return profile.getKills();
    }

    @Override
    public void onKillPlayer(Player player, Player attacker) {
        Profile profile = plugin.getProfileManager().getProfile(attacker.getUniqueId());
        if (profile.getKills() >= getGoal()) {
            plugin.getQuestInterface().onQuestComplete(attacker, this);
        }
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
