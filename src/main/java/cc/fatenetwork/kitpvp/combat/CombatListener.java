package cc.fatenetwork.kitpvp.combat;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kbase.utils.StringUtil;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.combat.events.CombatLogEvent;
import cc.fatenetwork.kitpvp.combat.events.CombatTagEvent;
import cc.fatenetwork.kitpvp.combat.events.TeleportToSpawnEvent;
import cc.fatenetwork.kitpvp.profiles.Profile;
import lombok.SneakyThrows;
import net.mineaus.lunar.api.LunarClientAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class CombatListener implements Listener {
    private final KitPvP plugin;

    public CombatListener(KitPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();
            CombatTagEvent e = new CombatTagEvent(player, attacker);
            Bukkit.getPluginManager().callEvent(e);
            plugin.getCombatManager().setCombatTime(player, 30);
            plugin.getCombatManager().setCombatTime(attacker, 30);
            plugin.getCombatManager().setCombatSet(player, true);
            plugin.getCombatManager().setCombatSet(attacker, true);
        }
    }

    @SneakyThrows
    @EventHandler
    public void onThrow(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            if (event.getEntity() instanceof EnderPearl) {
                Player player = (Player) event.getEntity().getShooter();
                if (!plugin.getCombatManager().isEnderpearlCooldown(player)) {
                    plugin.getCombatManager().setEnderpealSet(player, true);
                    if (ClientAPI.isClient(player)) {
                        LunarClientAPI.INSTANCE().sendCooldown(player, "Enderpearl", Material.ENDER_PEARL, 16);
                    }
                } else {
                    event.setCancelled(true);
                    player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                    player.updateInventory();
                }
            }
        }
    }

    @SneakyThrows
    @EventHandler
    public void onCombatTag(CombatTagEvent event) {
        Player player = event.getPlayer();
        Player attacker = event.getAttacker();
        if (ClientAPI.isClient(player)) {
            ClientAPI.sendCooldown(player, "You are now in combat", 30L, Material.DIAMOND_SWORD);
        }
        if (ClientAPI.isClient(attacker)) {
            ClientAPI.sendCooldown(attacker, "You are now in combat", 30L, Material.DIAMOND_SWORD);
        }
    }

    @EventHandler
    public void onCombatLog(CombatLogEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        double bal = profile.getBalance();
        double tax = plugin.getConfig().getDouble("server.kitpvp.tax");
        double fin = bal * tax;
        profile.setBalance(bal - fin);
        plugin.getEconomyManager().setServerMoney(plugin.getEconomyManager().getServerMoney() + fin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (plugin.getCombatManager().isSpawn(player) && (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ())) {
            player.sendMessage(StringUtil.format("&cYou have moved, teleportation cancelled."));
            plugin.getCombatManager().setSpawnSet(player, false);
        }
    }
}
