package cc.fatenetwork.kitpvp.stats;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsManager implements StatsInterface {
    @Getter private Map<String, Integer> killsMap = new HashMap<>();
    private final KitPvP plugin;

    public StatsManager(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public int getTopStat(int position) {
        if (position < 6 && position > 0) {
            return 0;
        }
        return 0;
    }

    @Override
    public String getPlayerTopStat(int position) {
        return null;
    }


    @Override
    public void applyKill(Player player) {
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        profile.setKills(profile.getKills() + 1);
        killsMap.remove(player.getName());
        killsMap.put(player.getName(), profile.getKills());
    }

    @Override
    public void applyDeath(Player player) {

    }

    @Override
    public void applyKillstreak(Player player) {

    }

    @Override
    public void updateTopStatPos(int pos, Player player) {

    }

    @Override
    public void updateMap() {
        killsMap = killsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        plugin.getMongoManager().update("KitPvP");
    }

}
