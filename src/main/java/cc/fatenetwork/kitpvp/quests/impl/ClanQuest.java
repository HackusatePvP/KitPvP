package cc.fatenetwork.kitpvp.quests.impl;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.quests.Quest;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClanQuest extends Quest {


    public ClanQuest() {
        super("Clan Quest", KitPvP.getPlugin());
    }

    @Override
    public String getName() {
        return "Clan Quest";
    }

    @Override
    public String getDescription() {
        return "Create or join a clan.";
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.BEACON);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(StringUtil.format("&6Clan Quest"));
        List<String> lore = new ArrayList<>();
        lore.add("&7&m-------------------------");
        lore.add("&c" + getDescription());
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
        return 0;
    }

    @Override
    public int getProgress(Profile profile) {
        return 0;
    }
}
