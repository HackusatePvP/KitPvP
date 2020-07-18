package cc.fatenetwork.kitpvp.kits;

import cc.fatenetwork.kbase.utils.BukkitUtils;
import cc.fatenetwork.kbase.utils.commands.ArgumentExecutor;
import cc.fatenetwork.kbase.utils.commands.CommandArgument;
import cc.fatenetwork.kbase.utils.commands.CommandWrapper;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.kits.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitExecutor extends ArgumentExecutor {
	private final KitPvP plugin;

	public KitExecutor(KitPvP plugin){
		super("kit");
		this.plugin = plugin;
		this.addArgument(new KitApplyArgument(plugin));
		this.addArgument(new KitCreateArgument(plugin));
		this.addArgument(new KitDeleteArgument(plugin));
		this.addArgument(new KitSetDescriptionArgument(plugin));
		this.addArgument(new KitGuiArgument(plugin));
		this.addArgument(new KitRenameArgument(plugin));
		this.addArgument(new KitSetDelayArgument(plugin));
		this.addArgument(new KitSetImageArgument(plugin));
		this.addArgument(new KitSetItemsArgument(plugin));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		String permission;
		if(args.length < 1){
			CommandWrapper.printUsage(sender, label, this.arguments);
			sender.sendMessage(ChatColor.GREEN + "/" + label + " <kitName> " + ChatColor.GRAY + "- Applies a kit.");
			return true;
		}
		CommandArgument argument = this.getArgument(args[0]);
		String string = permission = argument == null ? null : argument.getPermission();
		if(argument == null || permission != null && !sender.hasPermission(permission)){
			String kitPermission;
			Kit kit = this.plugin.getKitsManager().getKit(args[0]);
			if(sender instanceof Player && kit != null && ((kitPermission = kit.getPermissionNode()) == null || sender.hasPermission(kitPermission))){
				Player player = (Player) sender;
				kit.applyTo(player, false, true);
				return true;
			}
			sender.sendMessage(ChatColor.RED + "Kit or command " + args[0] + " not found.");
			return true;
		}
		argument.onCommand(sender, command, label, args);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		if(args.length != 1){
			return super.onTabComplete(sender, command, label, args);
		}
		List<String> previous = super.onTabComplete(sender, command, label, args);
		ArrayList<String> kitNames = new ArrayList<String>();
		for(Kit kit : this.plugin.getKitsManager().getKits()){
			String permission = kit.getPermissionNode();
			if(permission != null && !sender.hasPermission(permission)) continue;
			kitNames.add(kit.getName());
		}
		if(previous == null || previous.isEmpty()){
			previous = kitNames;
		}else{
			previous = new ArrayList<String>(previous);
			previous.addAll(0, kitNames);
		}
		return BukkitUtils.getCompletions(args, previous);
	}
}