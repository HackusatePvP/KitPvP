package cc.fatenetwork.kitpvp.commands;

import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        player.sendMessage(StringUtil.format("&cCopyright (c) 2020 FateNetwork));"));
        return false;
    }
}
