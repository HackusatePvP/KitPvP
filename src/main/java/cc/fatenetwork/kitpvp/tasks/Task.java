package cc.fatenetwork.kitpvp.tasks;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.broadcast.Broadcast;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Task extends BukkitRunnable {
    private final KitPvP plugin;
    private int count = 0;

    public Task(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        count++;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.getCombatManager().isCombat(player)) {
                int count = plugin.getCombatManager().getCombatTime(player);
                --count;
                plugin.getCombatManager().setCombatTime(player, count);
                if (count == 0) {
                    player.sendMessage(StringUtil.format("&aYou are no longer in combat"));
                    plugin.getCombatManager().getCombatSet().remove(player);
                    plugin.getCombatManager().getTimeMap().remove(player);
                }
            }
            if (plugin.getCombatManager().isSpawn(player)) {
                int count = plugin.getCombatManager().getSpawnTime(player);
                --count;
                plugin.getCombatManager().setSpawnTime(player, count);
                if (count == 0) {
                    player.sendMessage(StringUtil.format("&aYou have teleported to spawn."));
                    plugin.getCombatManager().getSpawnSet().remove(player);
                    plugin.getCombatManager().getSpawnMap().remove(player);
                    player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                }
            }
            if (plugin.getCombatManager().isEnderpearlCooldown(player)) {
                int count = plugin.getCombatManager().getEnderpearlTime(player);
                --count;
                plugin.getCombatManager().setEnderpearlTime(player, count);
                if (count == 0) {
                    player.sendMessage(StringUtil.format("&aYou can now use an enderpearl."));
                    plugin.getCombatManager().getEnderpealSet().remove(player);
                    plugin.getCombatManager().getEnderpealMap().remove(player);
                }
            }
            if (count == 60) {
                if (plugin.getBroadcastManager().getBroadcastMap().values().stream().findAny().isPresent()) {
                    Broadcast broadcast = plugin.getBroadcastManager().getBroadcastMap().values().stream().findAny().get();
                    List<String> message = new ArrayList<>();
                    message.add("");
                    message.add("");
                    message.add(broadcast.getMessage());
                    message.add("");
                    message.add("");
                    message.forEach(msg -> player.sendMessage(StringUtil.format(msg)));
                }
            }
            if (count == 220) {
                if (plugin.getBroadcastManager().getBroadcastMap().values().stream().findAny().isPresent()) {
                    Broadcast broadcast = plugin.getBroadcastManager().getBroadcastMap().values().stream().findAny().get();
                    List<String> message = new ArrayList<>();
                    message.add("");
                    message.add("");
                    message.add(broadcast.getMessage());
                    message.add("");
                    message.add("");
                    message.forEach(msg -> player.sendMessage(StringUtil.format(msg)));
                }
            }
        }
        if (count == 300) {
            count = 0;
        }
    }
}
