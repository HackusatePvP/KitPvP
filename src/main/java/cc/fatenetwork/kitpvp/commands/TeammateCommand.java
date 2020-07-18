package cc.fatenetwork.kitpvp.commands;

import cc.fatenetwork.kbase.utils.ClientAPI;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeammateCommand implements CommandExecutor {

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                return true;
            }
            ClientAPI.sendTeamMate(player, target);
        }
        return false;
    }
}
