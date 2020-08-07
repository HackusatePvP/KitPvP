package cc.fatenetwork.kitpvp.profiles;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class Profile {
    private UUID uuid, clanUUID;
    private String name, kit, activeQuest, clanName;
    private int kills,deaths,killstreak,objective,level;
    private long firstPlayed;
    private boolean chat,joinmsg,scoreboard;
    private boolean clan;
    private double balance,xp;
    private ArrayList<String> quests;

    public Profile(UUID uuid) {
        this.uuid = uuid;

    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
