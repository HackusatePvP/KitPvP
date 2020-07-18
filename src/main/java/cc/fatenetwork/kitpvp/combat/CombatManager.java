package cc.fatenetwork.kitpvp.combat;

import lombok.Getter;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class CombatManager {
    private final Set<Player> combatSet = new HashSet<>();
    private final Set<Player> enderpealSet = new HashSet<>();
    private final Map<Player, Integer> timeMap = new HashMap<>();
    private final Map<Player, Integer> enderpealMap = new HashMap<>();
    private final HashMap<Player, Player> targetMap = new HashMap<>();

    public void setCombatSet(Player player, boolean b) {
        if (b) {
            combatSet.add(player);
            timeMap.put(player, 30);
        } else {
            combatSet.remove(player);
            timeMap.remove(player);
        }
    }

    public void setCombatTime(Player player, int time) {
        timeMap.remove(player);
        timeMap.put(player, time);
    }

    public void setEnderpealSet(Player player, boolean b) {
        if (b) {
            enderpealSet.add(player);
            enderpealMap.put(player, 16);
        } else {
            enderpealSet.remove(player);
            enderpealMap.remove(player);
        }
    }

    public void setEnderpearlTime(Player player, int time) {
        enderpealMap.remove(player);
        enderpealMap.put(player, time);
    }

    public boolean isCombat(Player player) {
        return combatSet.contains(player);
    }

    public boolean isEnderpearlCooldown(Player player) {
        return enderpealSet.contains(player);
    }

    public int getCombatTime(Player player) {
       return timeMap.get(player);
    }

    public int getEnderpearlTime(Player player) {
        return enderpealMap.get(player);
    }


}
