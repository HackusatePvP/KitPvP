package cc.fatenetwork.kitpvp.quests;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestCommand implements CommandExecutor {
    private final KitPvP plugin;

    public QuestCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        player.openInventory(plugin.getGuiManager().getQuestGUI().getQuestGUI(profile));
        return false;
    }
}
