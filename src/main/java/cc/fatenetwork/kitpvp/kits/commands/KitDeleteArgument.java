package cc.fatenetwork.kitpvp.kits.commands;

import cc.fatenetwork.kbase.utils.commands.CommandArgument;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.kits.Kit;
import cc.fatenetwork.kitpvp.kits.events.KitRemoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitDeleteArgument extends CommandArgument {
	private final KitPvP plugin;

	public KitDeleteArgument(KitPvP plugin){
		super("delete", "Deletes a kit");
		this.plugin = plugin;
		this.aliases = new String[]{"del", "remove"};
		this.permission = "command.kit.argument." + this.getName();
	}

	@Override
	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName() + " <kitName>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length < 2){
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		Kit kit = this.plugin.getKitsManager().getKit(args[1]);
		if(kit == null){
			sender.sendMessage(ChatColor.RED + "There is not a kit named " + args[1] + '.');
			return true;
		}
		KitRemoveEvent event = new KitRemoveEvent(kit);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()){
			return true;
		}
		this.plugin.getKitsManager().removeKit(kit);
		sender.sendMessage(ChatColor.GRAY + "Removed kit '" + args[1] + "'.");
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