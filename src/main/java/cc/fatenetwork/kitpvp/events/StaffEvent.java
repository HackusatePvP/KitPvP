package cc.fatenetwork.kitpvp.events;

import cc.fatenetwork.kbase.staff.events.StaffModeDisableEvent;
import cc.fatenetwork.kbase.staff.events.StaffModeEnableEvent;
import cc.fatenetwork.kbase.staff.events.StaffVanishEvent;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StaffEvent implements Listener {
    private final KitPvP plugin;

    public StaffEvent(KitPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onStaffModeEnable(StaffModeEnableEvent event) {
        Player player = event.getPlayer();
        if (plugin.getCombatManager().isCombat(player)) {
            player.sendMessage(StringUtil.format("&cYou cannot enter staff-mode whilst in combat,"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onStaffModeDisable(StaffModeDisableEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(StringUtil.format("&cYou have disabled your staff-mode"));
    }

    @EventHandler
    public void onVanish(StaffVanishEvent event) {
        Player player = event.getPlayer();
        if (plugin.getCombatManager().isCombat(player)) {
            player.sendMessage(StringUtil.format("&cYou cannot vanish whilst in combat."));
            event.setCancelled(true);
        }
    }
}
