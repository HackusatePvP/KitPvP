package cc.fatenetwork.kitpvp.clans;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kbase.utils.chat.ClickAction;
import cc.fatenetwork.kbase.utils.chat.Text;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.clans.events.ClanCreateEvent;
import cc.fatenetwork.kitpvp.clans.events.ClanInviteEvent;
import cc.fatenetwork.kitpvp.clans.events.ClanJoinEvent;
import cc.fatenetwork.kitpvp.clans.events.ClanLeaveEvent;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClanCommand implements CommandExecutor {
    private final KitPvP plugin;
    private Map<UUID, Clan> disbandMap = new HashMap<>();

    public ClanCommand(KitPvP plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        if (args.length == 0) {
            List<String> message = new ArrayList<>();
            message.add("&7&m--------------------------------------------");
            message.add("&4&lClan &7Help");
            message.add("");
            message.add("&4* &c/clan create <name> <prefix>");
            message.add("&7&m--------------------------------------------");
            message.forEach(msg -> player.sendMessage(StringUtil.format(msg)));
        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("create")) {
                    player.sendMessage(StringUtil.format("&c/clan create <name> <prefix>"));
                }
                if (args[0].equalsIgnoreCase("invite")) {
                    player.sendMessage(StringUtil.format("&c/clan invite <player>"));
                }
                if (args[0].equalsIgnoreCase("join")) {
                    player.sendMessage(StringUtil.format("&c/clan join <name>"));
                }
                if (args[0].equalsIgnoreCase("disband")) {
                    player.sendMessage(StringUtil.format("&c/clan disband <name>"));
                }
                if (args[0].equalsIgnoreCase("invites")) {
                    if (profile.isClan()) {
                        Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                        if (clan == null) {
                            player.sendMessage(StringUtil.format("&cClan doesn ot exist."));
                            return true;
                        }
                        for (String a : clan.getInvited()) {
                            player.sendMessage(StringUtil.format("&7* &9" + a));
                        }
                    } else {
                        player.sendMessage(StringUtil.format("&cYou are not in a clan."));
                    }
                }
                if (args[0].equalsIgnoreCase("leave")) {
                    Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                    if (clan == null) {
                        player.sendMessage(StringUtil.format("&cYou are not in a clan."));
                        return true;
                    }

                    ClanLeaveEvent event = new ClanLeaveEvent(clan, player);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return true;
                    }
                    plugin.getClanManager().removeFromClan(player, clan);
                }

                // in Clan commands
                if (args[0].equalsIgnoreCase("open")) {
                    if (!profile.isClan()) {
                        player.sendMessage(StringUtil.format("&cYou must be in a clan to execute this command."));
                        return true;
                    }
                    Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                    if (clan == null) {
                        player.sendMessage(StringUtil.format("&cCould not find clan."));
                        return true;
                    }
                    if (!clan.getElites().contains(player.getName())) {
                        player.sendMessage(StringUtil.format("&cYou must be an elite member or higher to execute this command."));
                        return true;
                    }
                    if (clan.isOpen()) {
                        clan.setOpen(false);
                        player.sendMessage(StringUtil.format("&cYou have closed the clan."));
                        return true;
                    }
                    player.sendMessage(StringUtil.format("&aThe clan is now opened."));
                    clan.setOpen(true);
                }
                if (args[0].equalsIgnoreCase("teamdamage")) {
                    if (!profile.isClan()) {
                        player.sendMessage(StringUtil.format("&cYou must be in a clan to execute this command."));
                        return true;
                    }
                    Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                    if (clan == null) {
                        player.sendMessage(StringUtil.format("&cCould not find clan."));
                        return true;
                    }
                    if (!clan.getElites().contains(player.getName())) {
                        player.sendMessage(StringUtil.format("&cYou must be an elite member or higher to execute this command."));
                        return true;
                    }
                    if (clan.isTeamDamage()) {
                        clan.setTeamDamage(false);
                        player.sendMessage(StringUtil.format("&cYou have disabled team damage."));
                        for (Player member : clan.getOnline()) {
                            if (ClientAPI.isClient(member)) {
                                ClientAPI.sendNotification(member, "Team damage is now disabled", 5);
                            }
                        }
                        return true;
                    }
                    clan.setTeamDamage(true);
                    player.sendMessage(StringUtil.format("&cYou have &aenabled &7team damage."));
                    for (Player member : clan.getOnline()) {
                        if (ClientAPI.isClient(member)) {
                            ClientAPI.sendNotification(member, "Team damage is now enabled", 5);
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("settings")) {
                    if (!profile.isClan()) {
                        player.sendMessage(StringUtil.format("&cYou must be in a clan to execute this command."));
                        return true;
                    }
                    Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                    if (clan == null) {
                        player.sendMessage(StringUtil.format("&cCould not find clan."));
                        return true;
                    }
                    if (!clan.getElites().contains(player.getName())) {
                        player.sendMessage(StringUtil.format("&cYou must be an elite member or higher to execute this command."));
                        return true;
                    }
                    player.openInventory(plugin.getGuiManager().getClanGUI().getClanSettings(clan));
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    player.sendMessage(StringUtil.format("&c/clan create <" + args[1] + "> <prefix>"));
                }
                if (args[0].equalsIgnoreCase("rival")) {
                    if (!profile.isClan()) {
                        player.sendMessage(StringUtil.format("&cYou are not in a clan."));
                        return true;
                    }
                    Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                    if (clan == null) {
                        player.sendMessage(StringUtil.format("&cClan not found."));
                        return true;
                    }
                    if (!(clan.getLeader() == player.getUniqueId())) {
                        player.sendMessage(StringUtil.format("&cOnly leaders can rival other clans."));
                        return true;
                    }

                    String attempt = args[1];
                    Clan rival = plugin.getClanManager().getClan(attempt);
                    if (attempt == null) {
                        player.sendMessage(StringUtil.format("&cCould not find clan with name."));
                        return true;
                    }
                    if (plugin.getClanManager().rivalMap().containsKey(clan)) {
                        Clan found = plugin.getClanManager().rivalMap().get(clan);
                        if (found.getName().equals(rival.getName())) {
                            plugin.getClanManager().acceptRivalRequest(clan, rival);
                        } else if (plugin.getClanManager().rivalMap().get(rival).getName().equals(clan.getName())) {
                            plugin.getClanManager().acceptRivalRequest(rival, clan);
                        } else {
                            plugin.getClanManager().sendRivalRequest(clan, rival);
                        }
                    }

                    /*for (Clan attempt : plugin.getClanManager().rivalMap().values()) {
                        if (attempt.getName().equals(clan.getName())) {

                        }
                    }*/
                }
                if (args[0].equalsIgnoreCase("disband")) {
                    if (!args[1].equalsIgnoreCase(profile.getClanName())) {
                        return true;
                    }
                    if (!profile.isClan()) {
                        player.sendMessage(StringUtil.format("&cYou are not in a clan."));
                        return true;
                    }
                    Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                    if (clan == null) {
                        player.sendMessage(StringUtil.format("&cClan not found."));
                        return true;
                    }
                    if (!(clan.getLeader() == player.getUniqueId())) {
                        player.sendMessage(StringUtil.format("&cOnly the leader can disband the clan."));
                        return true;
                    }
                    if (!disbandMap.containsKey(player.getUniqueId())) {
                        new Text("You must re-type the ").setColor(ChatColor.GRAY).append("command").setUnderline(true).setHoverText("Click here to disband").setUnderline(false)
                                .setClick(ClickAction.RUN_COMMAND, "" + "/clan disband " + args[1]).append(" CONFIRM").setColor(ChatColor.GREEN).setBold(true).append(" this action.")
                                .setColor(ChatColor.GRAY).send(player);
                        disbandMap.put(player.getUniqueId(), clan);
                        plugin.getClanManager().removeClan(clan);
                        plugin.getMongoManager().delete(profile, clan);
                        return true;
                    }

                    player.sendMessage(StringUtil.format("&cYou have disbanded the clan."));
                    plugin.getMongoManager().delete(profile, clan);
                }
                if (args[0].equalsIgnoreCase("promote")) {
                    if (!profile.isClan()) {
                        player.sendMessage(StringUtil.format("&cYou are not in a clan."));
                        return true;
                    }
                    Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                    if (clan == null) {
                        player.sendMessage(StringUtil.format("&cClan not found."));
                        return true;
                    }
                    if (!(clan.getLeader() == player.getUniqueId())) {
                        player.sendMessage(StringUtil.format("&cOnly the leader can promote."));
                        return true;
                    }
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        player.sendMessage(StringUtil.format("&cTarget not found."));
                        return true;
                    }
                    Profile tar = plugin.getProfileManager().getProfile(target.getUniqueId());
                    if (!tar.isClan()) {
                        player.sendMessage(StringUtil.format("&cTarget is not in a clan."));
                        return true;
                    }
                    if (!(tar.getClanUUID() == profile.getClanUUID())) {
                        player.sendMessage(StringUtil.format("&cTarget is not in your clan."));
                        return true;
                    }
                    if (clan.getElites().contains(target.getName())) {
                        player.sendMessage(StringUtil.format("&cYou cannot promote this player any further."));
                        return true;
                    }
                    clan.getElites().add(player.getName());
                    target.sendMessage(StringUtil.format("&aYou have been promoted to an elite."));
                    player.sendMessage(StringUtil.format("&7You have promoted &c" + target.getUniqueId() + " &7to an &aelite."));
                }
                if (args[0].equalsIgnoreCase("join")) {
                    if (profile.isClan()) {
                        player.sendMessage(StringUtil.format("&cYou are already in a clan."));
                        return true;
                    }
                    String attempt = args[1];
                    Clan clan = plugin.getClanManager().getClan(attempt);
                    if (clan == null) {
                        player.sendMessage(StringUtil.format("&cClan does not exist."));
                        return true;
                    }
                    if (clan.getInvited() == null) {
                        clan.setInvited(new ArrayList<String>());
                    }
                    if (!clan.getInvited().contains(player.getName())) {
                        player.sendMessage(StringUtil.format("&cYou were never invited to a clan."));
                        return true;
                    }
                    ClanJoinEvent event = new ClanJoinEvent(clan, player);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return true;
                    }
                    player.sendMessage(StringUtil.format("&7You have joined &4" + clan.getName()));
                    if (ClientAPI.isClient(player)) {
                        ClientAPI.sendNotification(player, "You have joined a clan.", 5);
                    }
                    plugin.getClanManager().joinClan(player, clan);
                    clan.getOnline().forEach(member -> member.sendMessage(StringUtil.format("&c" + player.getName() + " &7has joined the clan.")));
                    clan.getInvited().remove(player.getName());
                    plugin.getMongoManager().updateClan(clan);
                    plugin.getMongoManager().update(player.getUniqueId());
                }
                if (args[0].equalsIgnoreCase("invite")) {
                    if (!profile.isClan()) {
                        player.sendMessage(StringUtil.format("&cYou are not in a clan."));
                        return true;
                    }
                    Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                    if (clan == null) {
                        player.sendMessage(StringUtil.format("&cError."));
                        return true;
                    }
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        player.sendMessage(StringUtil.format("&cTarget not found."));
                        return true;
                    }
                    ClanInviteEvent event = new ClanInviteEvent(clan, player, target);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return true;
                    }
                    plugin.getClanManager().invitePlayer(player, target, clan);
                    clan.getInvited().add(target.getName());
                    clan.getOnline().forEach(player1 -> player1.sendMessage(StringUtil.format("&c" + player.getName() + " &7has invited &a" + target.getName() + " &7to the clan.")));
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("create")) {
                    String clan = args[1];
                    String prefix = args[2];
                    if (plugin.getClanManager().containClan(plugin.getClanManager().getClan(clan))) {
                        player.sendMessage(StringUtil.format("&cClan already exists."));
                        return true;
                    }
                    if (plugin.getClanManager().inClan(player)) {
                        player.sendMessage(StringUtil.format("&cYou are already in a clan. /clan leave"));
                        return true;
                    }
                    if (!clan.matches("[0-9a-zA-Z]*")) {
                        player.sendMessage(StringUtil.format("Your clan name can only contain letters"));
                        return true;
                    }
                    if (clan.contains("&")) {
                        player.sendMessage(StringUtil.format("&cClan name can only contain letters."));
                        return true;
                    }
                    if (args[1].length() > 16) {
                        player.sendMessage(StringUtil.format("&cClan name cannot be more than 16 characters."));
                        return true;
                    }
                    if (!prefix.matches("[0-9a-zA-Z]*")) {
                        player.sendMessage(StringUtil.format("Your clan prefix can only contain letters"));
                        return true;
                    }
                    if (prefix.contains("&")) {
                        player.sendMessage(StringUtil.format("&cClan prefix can only contain letters."));
                        return true;
                    }
                    if (args[2].length() > 5) {
                        player.sendMessage(StringUtil.format("&cClan prefix cannot be more than 5 characters."));
                        return true;
                    }
                    UUID uuid = UUID.randomUUID();
                    Clan clan1 = new Clan(uuid);
                    ClanCreateEvent event = new ClanCreateEvent(clan1, player, clan, prefix);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return true;
                    }
                    clan1.setName(clan);
                    clan1.setPrefix(prefix);
                    clan1.setLeader(player.getUniqueId());
                    clan1.getOnline().add(player);
                    clan1.setMembers(new ArrayList<>());
                    clan1.setInvited(new ArrayList<>());
                    clan1.setElites(new ArrayList<>());
                    clan1.getOnline().add(player);
                    clan1.getMembers().add(player.getName());
                    clan1.getElites().add(player.getName());
                    clan1.setOpen(false);
                    clan1.setTeamDamage(false);
                    profile.setClanName(clan);
                    profile.setClanUUID(uuid);
                    plugin.getClanManager().createClan(clan1);
                    plugin.getMongoManager().insertClan(clan1.getUuid(), player);
                    profile.setClan(true);
                    plugin.getMongoManager().update(player.getUniqueId());
                    player.sendMessage(StringUtil.format("&aYou have created a clan."));
                }
            }
        }
        return false;
    }
}
