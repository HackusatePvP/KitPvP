package cc.fatenetwork.kitpvp.kits;

import cc.fatenetwork.kbase.utils.JavaUtils;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.kits.events.KitCreateEvent;
import cc.fatenetwork.kitpvp.kits.events.KitRemoveEvent;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor {
    private final KitPvP plugin;

    public KitCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            List<String> message = new ArrayList<>();
            message.add("&7&m----------------------------------");
            message.add("&4&lKit &8Help");
            message.add("&7&m----------------------------------");
            message.forEach(msg -> player.sendMessage(StringUtil.format(msg)));
        }
        if (args.length > 0) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("preview")) {
                    player.sendMessage(StringUtil.format("&cUsage: /kit preview <kit>"));
                }
                String kitAttempt = args[0];
                Kit kit = plugin.getKitsManager().getKit(kitAttempt);
                if (kit == null) {
                    player.sendMessage(StringUtil.format("&cKit not found."));
                    return true;
                }
                if (!player.hasPermission(kit.getPermissionNode())) {
                    player.sendMessage(StringUtil.format("&cYou do not have access to this kit."));
                    return true;
                }
                kit.applyTo(player, false, true);
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("preview")) {
                    Kit kit = plugin.getKitsManager().getKit(args[1]);
                    if (kit == null) {
                        player.sendMessage(StringUtil.format("&cKit not found."));
                        return true;
                    }
                    Inventory i = kit.getPreview(player);
                    KitListener.getPreviewInventory().add(i);
                    player.openInventory(i);
                }
            }
                if (args.length == 1) {
                    if (player.hasPermission("kits.admin")) {
                        if (args[0].equalsIgnoreCase("create")) {
                            player.sendMessage(StringUtil.format("&cUsage: /create <kitName> <description>"));
                        }
                        if (args[0].equalsIgnoreCase("delete")) {
                            player.sendMessage(StringUtil.format("&cUsage: /delete <kitName>"));
                        }
                    }
                }
                if (args.length == 2) {
                    if (player.hasPermission("kits.admin")) {
                        if (args[0].equalsIgnoreCase("setitems")) {
                            Kit kit = plugin.getKitsManager().getKit(args[1]);
                            if (kit == null) {
                                player.sendMessage(StringUtil.format("&cKit not found."));
                                return true;
                            }
                            PlayerInventory inventory = player.getInventory();
                            kit.setItems(inventory.getContents());
                            kit.setArmor(inventory.getArmorContents());
                            sender.sendMessage(ChatColor.AQUA + "Set the items of kit " + kit.getDisplayName() + " as your current inventory.");
                        }
                        if (args[0].equalsIgnoreCase("disable")) {
                            Kit kit = plugin.getKitsManager().getKit(args[1]);
                            if (kit != null) {
                                kit.setEnabled(!kit.isEnabled());
                                player.sendMessage(StringUtil.format("&7You have disabled &4" + kit.getName()));
                            }
                        }
                        if (args[0].equalsIgnoreCase("delete")) {
                            Kit kit = plugin.getKitsManager().getKit(args[1]);
                            if (kit == null) {
                                player.sendMessage(StringUtil.format("&cKit not found."));
                                return true;
                            }
                            KitRemoveEvent event = new KitRemoveEvent(kit);
                            Bukkit.getPluginManager().callEvent(event);
                            if (event.isCancelled()) {
                                return true;
                            }
                            plugin.getKitsManager().removeKit(kit);
                            player.sendMessage(StringUtil.format("&7You have &cdeleted &7kit &4&l" + kit.getName()));
                        }
                    }
                }
                if (args.length > 3) {
                    if (player.hasPermission("kits.admin")) {
                        if (args[0].equalsIgnoreCase("setdelay")) {
                            Kit kit = plugin.getKitsManager().getKit(args[1]);
                            if (kit == null) {
                                player.sendMessage(StringUtil.format("&cKit not found."));
                                return true;
                            }
                            long duration = JavaUtils.parse(args[2]);
                            if (duration == -1) {
                                player.sendMessage(StringUtil.format("&cInvalid duration, use the correct format: 1min 1s"));
                                return true;
                            }
                            kit.setDelay(duration);
                            sender.sendMessage(ChatColor.YELLOW + "Set delay of kit " + kit.getName() + " to " + DurationFormatUtils.formatDurationWords(duration, true, true) + '.');
                        }
                        if (args[0].equalsIgnoreCase("create")) {
                            if (!JavaUtils.isAlphanumeric(args[1])) {
                                player.sendMessage(StringUtil.format("&cKit names can only be alphanumeric."));
                                return true;
                            }
                            Kit kit = plugin.getKitsManager().getKit(args[1]);
                            if (kit != null) {
                                player.sendMessage(StringUtil.format("&cThere is already a kit named " + args[1]));
                                return true;
                            }
                            kit = new Kit(args[1], args.length >= 3 ? args[2] : null, player.getInventory());
                            KitCreateEvent event = new KitCreateEvent(kit);
                            Bukkit.getPluginManager().callEvent(event);
                            if (event.isCancelled()) {
                                return true;
                            }
                            plugin.getKitsManager().createKit(kit);
                            player.sendMessage(StringUtil.format("&7You have created kit &4&l" + kit.getName()));
                        }
                    }
                }
            }
        return false;
    }
}
