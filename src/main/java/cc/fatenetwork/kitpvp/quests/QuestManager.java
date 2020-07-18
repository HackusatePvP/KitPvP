package cc.fatenetwork.kitpvp.quests;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.quests.events.QuestCompleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuestManager implements QuestInterface{
    private final KitPvP plugin;

    public QuestManager(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> quests(Player var1) {
        Profile profile = plugin.getProfileManager().getProfile(var1.getUniqueId());
        return profile.getQuests();
    }

    @Override
    public String getActiveQuest(Player var1) {
        Profile profile = plugin.getProfileManager().getProfile(var1.getUniqueId());
        return profile.getActiveQuest();
    }

    @Override
    public int getQuestGoal(Player var1) {
        Profile profile = plugin.getProfileManager().getProfile(var1.getUniqueId());
        return Quest.getByName(profile.getActiveQuest()).getGoal();
    }

    @Override
    public int getQuestCompletion(Player var1) {
        Profile profile = plugin.getProfileManager().getProfile(var1.getUniqueId());
        return Quest.getByName(profile.getActiveQuest()).getProgress(profile);
    }

    @Override
    public double getQuestProgress(Player var1) {
        Profile profile = plugin.getProfileManager().getProfile(var1.getUniqueId());
        return (double) (getQuestCompletion(var1) / getQuestGoal(var1));
    }

    @Override
    public void onQuestComplete(Player var1, Quest var2) {
        QuestCompleteEvent event = new QuestCompleteEvent(var1, var2);
        Bukkit.getPluginManager().callEvent(event);
    }
}
