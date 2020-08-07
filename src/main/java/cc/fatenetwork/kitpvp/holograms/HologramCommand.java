package cc.fatenetwork.kitpvp.holograms;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HologramCommand implements CommandExecutor {
    private final KitPvP plugin;

    public HologramCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("kitpvp.hologram.create")) {
            player.sendMessage(StringUtil.format("&cYou do not have permissions to execute this command."));
            return true;
        }
        if (!ClientAPI.isClient(player)) {
            player.sendMessage(StringUtil.format("&cYou must be on lunar to execute this command."));
            return true;
        }
        if (args.length == 0) {

        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("create")) {
                    player.sendMessage(StringUtil.format("&c/hologram create <name> String.. <lines>"));
                }
                if (args[0].equalsIgnoreCase("edit")) {
                    player.sendMessage(StringUtil.format("&c/hologram edit <name> <title|line> <name|line <int>> <String line>"));
                }
            }
            if (args.length == 2) {

            }
            if (args.length == 3) {

            } else {
                if (args[0].equalsIgnoreCase("create")) {
                    Hologram hologram = plugin.getHologramInterface().getHologram(args[1]);
                    if (hologram != null) {
                        player.sendMessage(StringUtil.format("&cHologram with that name already exists."));
                        return true;
                    }
                    String lines = "";
                    for (int i = 2; i < args.length; i++ ) {
                        lines = lines + args[i];
                    }
                    hologram = new Hologram(args[1], player.getLocation(), true, lines);
                    plugin.getHologramInterface().createHologram(hologram);
                    player.sendMessage(StringUtil.format("&cYou have created a new hologram"));
                }
            }
        }
        return false;
    }
}
