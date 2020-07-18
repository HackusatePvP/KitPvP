package cc.fatenetwork.kitpvp.kits.commands;

import cc.fatenetwork.kbase.utils.commands.CommandArgument;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitSetItemsArgument extends CommandArgument {
	private final KitPvP plugin;

	public KitSetItemsArgument(KitPvP plugin){
		super("setitems", "Sets the items of a kit");
		this.plugin = plugin;
		this.permission = "base.command.kit.argument." + this.getName();
	}

	@Override
	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName() + " <kitName>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "Only players can set kit items.");
			return true;
		}
		if(args.length < 2){
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		Kit kit = this.plugin.getKitsManager().getKit(args[1]);
		if(kit == null){
			sender.sendMessage(ChatColor.RED + "Kit '" + args[1] + "' not found.");
			return true;
		}
		Player player = (Player) sender;
		PlayerInventory inventory = player.getInventory();
		kit.setItems(inventory.getContents());
		kit.setArmour(inventory.getArmorContents());
		sender.sendMessage(ChatColor.AQUA + "Set the items of kit " + kit.getDisplayName() + " as your current inventory.");
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