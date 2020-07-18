package cc.fatenetwork.kitpvp.quests;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.quests.events.QuestCompleteEvent;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.concurrent.TimeUnit;

public class QuestListener implements Listener {
    private final KitPvP plugin;

    public QuestListener(KitPvP plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
    @EventHandler
    public void onQuestComplete(QuestCompleteEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        Quest quest = event.getQuest();
        if (ClientAPI.isClient(player)) {
            ClientAPI.sendNotification(player, "You have completed " + quest.getName() + ".", 5, TimeUnit.SECONDS);
        }
        if (quest.getNextQuest() != null) {
            profile.setActiveQuest(quest.getNextQuest());
        }
    }

    @EventHandler
    public void onKillPlayer(PlayerDeathEvent event) {
        if (event.getEntity() != null && event.getEntity().getKiller() != null) {
            Player player = event.getEntity();
            Player killer = event.getEntity().getKiller();
            Profile profile = plugin.getProfileManager().getProfile(killer.getUniqueId());
            if (profile.getActiveQuest() != null) {
                Quest quest = Quest.getByName(profile.getActiveQuest());
                quest.onKillPlayer(player);
            }
        }
    }
}
