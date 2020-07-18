package cc.fatenetwork.kitpvp.commands;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TestCommand implements CommandExecutor {
    private final KitPvP plugin;

    public TestCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        List<String> message = new ArrayList<>();
        message.add("&7&m--------------------------------------------------");
        message.add("&4&lTest");
        message.add("");
        message.add("Clan: " + profile.isClan());
        message.add("ClanUUID: " + profile.getClanUUID());
        message.add("ClanName " + profile.getClanName());
        message.add("&7&m--------------------------------------------------");
        message.forEach(msg -> player.sendMessage(StringUtil.format(msg)));
        return false;
    }
}
