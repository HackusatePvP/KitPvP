package cc.fatenetwork.kitpvp.quests;

import cc.fatenetwork.kitpvp.profiles.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Quest {
    private static Map<String, Quest> byName = new HashMap<>();

    private String name;

    public Quest(String name) {
        this.name = name;
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

    public void onKillPlayer(Player victim) {

    }

}
