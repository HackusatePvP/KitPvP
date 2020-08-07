package cc.fatenetwork.kitpvp.commands;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

public class BalanceCommand implements CommandExecutor {
    private final KitPvP plugin;

    public BalanceCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        if (args.length == 0) {
            player.sendMessage(StringUtil.format("&7Your balance is &a" + format.format(profile.getBalance())));
        } else {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                player.sendMessage(StringUtil.format("&cTarget not found."));
                return true;
            }
            player.sendMessage(StringUtil.format("&c" + target.getName() + "'s &7balance is &a" + format.format(plugin.getProfileManager().getProfile(target.getUniqueId()).getBalance())));
        }
        return false;
    }
}
