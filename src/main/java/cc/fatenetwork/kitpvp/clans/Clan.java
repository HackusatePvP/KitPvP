package cc.fatenetwork.kitpvp.clans;

import lombok.Data;
import org.bukkit.entity.Player;

import java.util.*;

@Data
public class Clan {
    private UUID uuid, leader;
    private String name, prefix;
    private ArrayList<String> members;
    private List<Player> online = new ArrayList<>();
    private ArrayList<String> invited;

    public Clan(UUID uuid) {
        this.uuid = uuid;
    }

}
