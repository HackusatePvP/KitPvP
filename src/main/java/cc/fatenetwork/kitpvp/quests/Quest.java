package cc.fatenetwork.kitpvp.quests;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.quests.impl.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Quest {
    private static Map<String, Quest> byName = new HashMap<>();
    private final KitPvP plugin;

    public static Quest STARTER_QUEST = new StarterQuest(KitPvP.getPlugin());
    public static Quest KILLSTREAK_I_QUEST = new KillStreakQuestI(KitPvP.getPlugin());
    public static Quest CLAN_QUEST = new ClanQuest();
    public static Quest KILL_QUEST_I = new KillQuestI("Kill Quest I", KitPvP.getPlugin());
    public static Quest KILLSTREAK_II_QUEST = new KillstreakQuestII("Kill Streak Quest II", KitPvP.getPlugin());
    public static Quest KILL_QUEST_II = new KillQuestII("Kill Quest II", KitPvP.getPlugin());

    private String name;

    public Quest(String name, KitPvP plugin) {
        this.name = name;
        this.plugin = plugin;
        byName.put(name, this);
    }

    public static Quest getByName(String name) {
        return byName.get(name);
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract ItemStack getItem();

    public abstract String getNextQuest();

    public abstract int getGoal();

    public abstract int getProgress(Profile profile);

    public void onComplete(Player player, Quest quest) {

    }

    public void onKillPlayer(Player victim, Player attacker) {

    }

}
