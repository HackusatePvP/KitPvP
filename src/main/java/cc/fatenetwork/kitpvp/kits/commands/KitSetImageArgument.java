package cc.fatenetwork.kitpvp.kits.commands;

import cc.fatenetwork.kbase.Base;
import cc.fatenetwork.kbase.utils.commands.CommandArgument;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitSetImageArgument extends CommandArgument {
	private final KitPvP plugin;

	public KitSetImageArgument(KitPvP plugin){
		super("setimage", "Sets the image of kit in GUI to held item");
		this.plugin = plugin;
		this.aliases = new String[]{"setitem", "setpic", "setpicture"};
		this.permission = "base.command.kit.argument." + this.getName();
	}

	@Override
	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName() + " <kitName>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This argument is only executable by players.");
			return true;
		}
		if(args.length < 2){
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		Player player = (Player) sender;
		ItemStack stack = player.getItemInHand();
		if(stack == null || stack.getType() == Material.AIR){
			player.sendMessage(ChatColor.RED + "You are not holding anything.");
			return true;
		}
		Kit kit = this.plugin.getKitsManager().getKit(args[1]);
		if(kit == null){
			sender.sendMessage(ChatColor.RED + "There is not a kit named " + args[1] + '.');
			return true;
		}
		kit.setImage(stack.clone());
		sender.sendMessage(ChatColor.AQUA + "Set image of kit " + ChatColor.YELLOW + kit.getDisplayName() + ChatColor.AQUA + " to " + ChatColor.YELLOW + Base.getPlugin().getItemDb().getName(stack) + ChatColor.AQUA + '.');
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