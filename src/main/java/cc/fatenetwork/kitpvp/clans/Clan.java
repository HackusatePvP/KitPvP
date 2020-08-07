package cc.fatenetwork.kitpvp.clans;

import lombok.Data;
import org.bukkit.entity.Player;

import java.util.*;

@Data
public class Clan {
    private UUID uuid, leader;
    private String name, prefix;
    private boolean teamDamage, open;
    private ArrayList<String> members;
    private ArrayList<String> elites;
    private ArrayList<String> invited;
    private ArrayList<String> rivals;
    private List<Player> online = new ArrayList<>();

    public Clan(UUID uuid) {
        this.uuid = uuid;
    }

}
