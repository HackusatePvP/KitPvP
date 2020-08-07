package cc.fatenetwork.kitpvp.economy;

import cc.fatenetwork.kbase.utils.JavaUtils;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCommand implements CommandExecutor {
    private final KitPvP plugin;

    public EconomyCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("kitpvp.admin")) {
            player.sendMessage(StringUtil.format("&cYou do not have permissions to execute this command."));
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(StringUtil.format("&cEco command"));
        } else {
            if (args.length == 1) {

            } else if (args.length == 2) {

            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set")) {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        player.sendMessage(StringUtil.format("&cTarget not found."));
                        return true;
                    }
                    Profile profile = plugin.getProfileManager().getProfile(target.getUniqueId());
                    double amount = Double.parseDouble(args[2]);
                    profile.setBalance(amount);
                    player.sendMessage(StringUtil.format("&aYou have set the balance of &c" + target.getName() + " &ato &e" + amount));
                }
            }
         }
        return false;
    }
}
