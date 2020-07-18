package cc.fatenetwork.kitpvp.commands;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CombatCommand implements CommandExecutor {
    private final KitPvP plugin;

    public CombatCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (plugin.getCombatManager().isCombat(player)) {
            player.sendMessage(StringUtil.format("&cYou are in combat for " + plugin.getCombatManager().getCombatTime(player) + "s"));
        } else {
            player.sendMessage(StringUtil.format("&aYou are not in combat."));
        }
        return false;
    }
}
