package cc.fatenetwork.kitpvp.tasks;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Task extends BukkitRunnable {
    private final KitPvP plugin;

    public Task(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
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
        }
    }
}
