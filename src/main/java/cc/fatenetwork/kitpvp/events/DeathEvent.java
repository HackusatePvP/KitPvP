package cc.fatenetwork.kitpvp.events;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.kits.Kits;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.NumberFormat;
import java.util.Random;

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
            killer.playSound(killer.getLocation(), Sound.NOTE_PLING, 1f, 1f);
            Random random = new Random();
            int bound = 10;
            int rand = random.nextInt(bound);
            if (rand > 2 && rand < 6) {
                killer.setHealth(20.0);
            } else if (rand > 7 && rand < 10) {
                killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            }
            double baseCredit = 3.0;
            Random r = new Random();
            int b = 12;
            int p = r.nextInt(b);
            double gained = baseCredit + p;
            NumberFormat format = NumberFormat.getCurrencyInstance();
            profile1.setBalance(profile1.getBalance() + gained);
            killer.sendMessage(StringUtil.format("&a+ &c" + format.format(gained)));

            double baseXp = 5.0;
            Random ran = new Random();
            int bo = 20;
            int xp = ran.nextInt(bo);
            double ganed = baseXp + xp;
            profile1.setXp(gained);
            plugin.getLevelManager().updateLevel(profile1);
            killer.sendMessage(StringUtil.format("&a+ &c" + ganed + " xp"));
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.DROWNING) {
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(StringUtil.format("&4" + player.getName() + "&7[&8" + profile.getKills() + "&7] has drowned to death")));
        } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.SUICIDE) {
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(StringUtil.format("&4" + player.getName() + "&7[&8" + profile.getKills() + "&7] has played themselves")));
        } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) {
            if (event.getEntity().getKiller() != null) {
                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(StringUtil.format("&4" + player.getName() + "&7[&8" + profile.getKills() + "&7] has cooked to death thanks to &c" + event.getEntity().getKiller().getName() + "&7[&8" + plugin.getProfileManager().getProfile(event.getEntity().getKiller().getUniqueId()).getKills()) + "&7]"));
            } else {
                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(StringUtil.format("&4" + player.getName() + "&7[&8" + profile.getKills() + "&7] was burned to a crisp")));
            }
        } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (event.getEntity().getKiller() != null) {
                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(StringUtil.format("&4" + player.getName() + "&7[&8" + profile.getKills() + "&7] was pushed off a cliff thanks to &c" + event.getEntity().getKiller().getName() + "&7[&8" + plugin.getProfileManager().getProfile(event.getEntity().getKiller().getUniqueId()).getKills()) + "&7]"));
            } else {
                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(StringUtil.format("&4" + player.getName() + "&7[&8" + profile.getKills() + "&7] has jumped off a cliff")));
            }
        } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(StringUtil.format("&c" + event.getEntity().getKiller().getName() + "&7[&8" + plugin.getProfileManager().getProfile(event.getEntity().getKiller().getUniqueId()).getKills() + "&7] killed &9" + player.getName() + "&7[&8" + profile.getKills() + "&7]")));

        } else {
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(StringUtil.format("&4" + player.getName() + "&7[&8" + profile.getKills() + "&7] has died from unknown causes.")));
        }
        player.spigot().respawn();
        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                player.getInventory().setArmorContents(Kits.DEFAULT_KIT.getArmorContents());
                player.getInventory().setContents(Kits.DEFAULT_KIT.getInventory().getContents());
            }
        }, 15L);
        player.updateInventory();
        profile.setDeaths(profile.getDeaths() + 1);
        profile.setKillstreak(0);
        plugin.getCombatManager().setCombatTime(player, 1);
    }

}
