package cc.fatenetwork.kitpvp.commands;

import cc.fatenetwork.kbase.utils.StringUtil;
import cc.fatenetwork.kbase.utils.chat.ClickAction;
import cc.fatenetwork.kbase.utils.chat.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LunarCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        player.sendMessage("");
        player.sendMessage("");
        Text text = new Text("Lunar").setColor(ChatColor.DARK_RED).setBold(true);
        text.append(new Text(" Client").setColor(ChatColor.DARK_GRAY));
        text.append(new Text("\n Lunar client is an all in one minecraft pvp client which offers many features that the server supports.").setColor(ChatColor.GRAY));
        text.append(new Text(" If you wish to download ").setColor(ChatColor.GRAY));
        text.append(new Text("Click here").setColor(ChatColor.GREEN).setUnderline(true).setClick(ClickAction.OPEN_URL, "" + "https://www.lunarclient.com/download/").setHoverText(StringUtil.format("&aClick to download lunar client.")));
        text.send(player);
        player.sendMessage("");
        player.sendMessage("");
        return false;
    }
}
