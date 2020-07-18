package cc.fatenetwork.kitpvp.events;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {
    private final KitPvP plugin;

    public DeathEvent(KitPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player player = event.getEntity(); //this is the player that died
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        profile.setKillstreak(0);
        profile.setDeaths(profile.getDeaths() + 1);
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            Profile profile1 = plugin.getProfileManager().getProfile(killer.getUniqueId());
            profile1.setKillstreak(profile.getKillstreak() + 1);
            profile1.setKills(profile1.getKills() + 1);
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(StringUtil.format("&c" + killer.getName() + "&7[&8" + profile1.getKills() + "&7] killed &9" + player.getName() + "&7[&8" + profile.getKills() + "&7]")));
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.DROWNING) {

            return;
        } else {
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(StringUtil.format("&9" + player.getName() + "&7[&8" + profile.getKills() + "&7] has died from unknown causes.")));
        }
        profile.setDeaths(profile.getDeaths() + 1);
        profile.setKillstreak(0);
    }

}
