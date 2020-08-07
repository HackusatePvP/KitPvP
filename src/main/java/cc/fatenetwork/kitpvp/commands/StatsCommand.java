package cc.fatenetwork.kitpvp.commands;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.clans.Clan;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StatsCommand implements CommandExecutor {
    private final KitPvP plugin;

    public StatsCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        if (args.length == 0) {
            List<String> message = new ArrayList<>();
            message.add("&7&m---------------------------------");
            message.add("&c&l" + player.getName() + "'s &8Statistics");
            message.add("&7Kills: &c" + profile.getKills());
            message.add("&7Deaths: &c" + profile.getDeaths());
            message.add("&7KDR: &c" ); //todo
            if (profile.isClan()) {
                Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                if (clan != null) {
                    message.add("");
                    message.add("&7Clan: &c" + clan.getName());
                    message.add("&7Leader: &c" + clan.getLeader());
                }
            }
            message.add("&7&m---------------------------------");
            message.forEach(msg -> player.sendMessage(StringUtil.format(msg)));

        }
        return false;
    }
}
