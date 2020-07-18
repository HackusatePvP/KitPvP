package cc.fatenetwork.kitpvp.kits.commands;

import cc.fatenetwork.kbase.utils.commands.CommandArgument;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.kits.Kit;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitSetDescriptionArgument extends CommandArgument {
	private final KitPvP plugin;

	public KitSetDescriptionArgument(KitPvP plugin){
		super("setdescription", "Sets the description of a kit");
		this.plugin = plugin;
		this.aliases = new String[]{"setdesc"};
		this.permission = "base.command.kit.argument." + this.getName();
	}

	@Override
	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName() + " <kitName> <none|description>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length < 3){
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		Kit kit = this.plugin.getKitsManager().getKit(args[1]);
		if(kit == null){
			sender.sendMessage(ChatColor.RED + "There is not a kit named " + args[1] + '.');
			return true;
		}
		if(args[2].equalsIgnoreCase("none") || args[2].equalsIgnoreCase("null")){
			kit.setDescription(null);
			sender.sendMessage(ChatColor.YELLOW + "Removed description of kit " + kit.getDisplayName() + '.');
			return true;
		}
		String description = ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 2, args.length));
		kit.setDescription(description);
		sender.sendMessage(ChatColor.YELLOW + "Set description of kit " + kit.getDisplayName() + " to " + description + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		if(args.length != 2){
			return Collections.emptyList();
		}
		List<Kit> kits = this.plugin.getKitsManager().getKits();
		ArrayList<String> results = new ArrayList<String>(kits.size());
		for(Kit kit : kits){
			results.add(kit.getName());
		}
		return results;
	}
}