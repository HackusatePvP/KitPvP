package cc.fatenetwork.kitpvp.commands;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.kits.Kits;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ApplyKitCommand implements CommandExecutor {
    private final KitPvP plugin;

    public ApplyKitCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        if (!plugin.getKitsManager().inRegion(player.getLocation(), "spawn")) {
            player.sendMessage(StringUtil.format("&cYou can only apply kits at spawn."));
            return true;
        }
        Kits kit = Kits.getByName(profile.getKit());
        if (kit != null) {
            player.getInventory().setArmorContents(kit.getArmorContents());
            player.getInventory().setContents(kit.getInventory().getContents());
            player.sendMessage(StringUtil.format("&aYou have applied kit &e" + kit.getName()));
        }
        return false;
    }
}
