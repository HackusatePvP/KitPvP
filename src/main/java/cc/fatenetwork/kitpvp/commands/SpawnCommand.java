package cc.fatenetwork.kitpvp.commands;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    private final KitPvP plugin;

    public SpawnCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (plugin.getCombatManager().isCombat(player)) {
            player.sendMessage(StringUtil.format("&cYou cannot go to spawn whilst in combat."));
            return true;
        }
        if (plugin.getCombatManager().isSpawn(player)) {
            player.sendMessage(StringUtil.format("&cYou are already teleporting."));
            return true;
        }
        player.sendMessage(StringUtil.format("&7You are teleporting in &c" + 5 + "s "));
        plugin.getCombatManager().setSpawnSet(player, true);
        return false;
    }
}
