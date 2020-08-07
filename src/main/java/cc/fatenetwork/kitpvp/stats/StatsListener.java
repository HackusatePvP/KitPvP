package cc.fatenetwork.kitpvp.stats;

import cc.fatenetwork.kitpvp.KitPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class StatsListener implements Listener {
    private final KitPvP plugin;

    public StatsListener(KitPvP plugin) {
        this.plugin = plugin;

    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity() != null && event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            plugin.getStatsInterface().applyKill(player);
        }
    }

}
