package cc.fatenetwork.kitpvp.stats;

import org.bukkit.entity.Player;

import java.util.Map;

public interface StatsInterface {

    Map<String, Integer> getKillsMap();

    int getTopStat(int position);

    String getPlayerTopStat(int position);

    void applyKill(Player player);

    void applyDeath(Player player);

    void applyKillstreak(Player player);

    void updateTopStatPos(int pos, Player player);

    void updateMap();

}
