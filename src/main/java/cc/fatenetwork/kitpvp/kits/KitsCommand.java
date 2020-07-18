package cc.fatenetwork.kitpvp.kits;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class KitsCommand implements CommandExecutor {
    private final KitPvP plugin;

    public KitsCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        List<Kit> kits = plugin.getKitsManager().getKits();
        if (kits.isEmpty()) {
            player.sendMessage(StringUtil.format("&cThere are no kits available."));
            return true;
        }
        player.openInventory(plugin.getKitsManager().getGui(player));
        return false;
    }
}
