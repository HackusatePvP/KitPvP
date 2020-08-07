package cc.fatenetwork.kitpvp.rank;

import cc.fatenetwork.kitpvp.KitPvP;
import org.bukkit.entity.Player;

public class RankManager {
    private final KitPvP plugin;

    public RankManager(KitPvP plugin) {
        this.plugin = plugin;
    }

    public String getRankPrefix(Player player) {
        if (plugin.getPerms().getPrimaryGroup(player).equalsIgnoreCase("master")) {
            return "&7[Master&7]";
        }
        if (plugin.getPerms().getPrimaryGroup(player).equalsIgnoreCase("saber")) {
            return "&7[&9Saber&7]";
        }
        if (plugin.getPerms().getPrimaryGroup(player).equalsIgnoreCase("fate")) {
            return "&7[&4Fate&7]";
        }
        return "&7[&fMember&7]";
    }
}
