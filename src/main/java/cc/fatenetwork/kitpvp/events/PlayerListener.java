package cc.fatenetwork.kitpvp.events;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.clans.Clan;
import cc.fatenetwork.kitpvp.clans.events.ClanMemberLoginEvent;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import lombok.SneakyThrows;
import net.mineaus.lunar.api.LunarClientAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {
    private final KitPvP plugin;

    public PlayerListener(KitPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //we must create a new profile when they join and then we can check the database to see if they have a profile saved. Just because they don't have a profile doesn't mean they
        // haven't played before
        Profile profile = new Profile(player.getUniqueId());
        profile.setQuests(new ArrayList<>());
        plugin.getProfileManager().profiles.put(player.getUniqueId(), profile);
        if (plugin.getMongoManager().getUUID(player.getUniqueId()) == null) { //we use this to see if they have a profile in the database
            //this means they do not so we have to insert them
            plugin.getMongoManager().insertPlayer(player.getUniqueId());
            profile.getQuests().add("Starter Quest");
            profile.setActiveQuest("Starter Quest");
            player.sendMessage(StringUtil.format("&aProfile created."));
            Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new BukkitRunnable() {
                @SneakyThrows
                @Override
                public void run() {
                    if (ClientAPI.isClient(player)) {
                        ClientAPI.sendTitle(player, false, StringUtil.format("&7Welcome to &4&lKitPvP"), 1f, 3, 1, 4);
                        LunarClientAPI.INSTANCE().sendNotification(player, "You have gianed a new quest", 4);
                        ClientAPI.setWayPoint(player, "Spawn", new Location(Bukkit.getWorld("world"), 0 ,70 ,0), 1, true, true);
                    }
                }
            }, 43L);
        } else {
            plugin.getMongoManager().getPlayer(player.getUniqueId()); //this will load the profile from the database
            player.sendMessage(StringUtil.format("&aProfile loaded.."));
        }
        if (profile.isClan()) {
            //This means they are in a clan, clan could be disbanded
            if (plugin.getMongoManager().getClan(profile.getClanUUID()) == null) {
                //This means the clan uuid doesn ot exist in the database
                //todo add disabnd method
            } else {
                plugin.getMongoManager().parseClan(profile.getClanUUID());
                Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getClanManager().addToClan(player, plugin.getClanManager().getClan(profile.getClanUUID()));
                        ClanMemberLoginEvent e = new ClanMemberLoginEvent(player, plugin.getClanManager().getClan(profile.getClanUUID()));
                        Bukkit.getPluginManager().callEvent(e);
                    }
                }, 30L);
            }
        }
        List<String> message = new ArrayList<>();
        message.add("&7&m-----------------------------------------");
        message.add("&7Welcome, &b" + player.getName() + " &7to &9KitPvP");
        message.add("&7Twitter: &9@FateKits");
        message.add("&7Website: &9www.fatenetwork.cc");
        message.add("&7&m-----------------------------------------");
        message.forEach(msg -> player.sendMessage(StringUtil.format(msg)));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getMongoManager().update(player.getUniqueId());
        plugin.getProfileManager().getProfiles().remove(player.getUniqueId());
        Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(StringUtil.format("&c- &7" + player.getName())));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        event.setCancelled(true);
        Bukkit.getOnlinePlayers().forEach(online -> {
            Profile pro = plugin.getProfileManager().getProfile(online.getUniqueId());
            if (pro.isChat()) {
                if (player.hasPermission("kitpvp.chat.format")) {
                    if (!profile.isClan()) {
                        online.sendMessage(StringUtil.format("&6" + player.getName() + "&8[&4" + profile.getLevel() + "&8] &7» &f" + event.getMessage()));
                    } else {
                        Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                        online.sendMessage(StringUtil.format("&7[&c" + clan.getName() + "&7 &6" + player.getName() + "&8[&4" + profile.getLevel() + "&8] &7» &f" + event.getMessage()));
                    }
                } else {
                    if (!profile.isClan()) {
                        online.sendMessage(StringUtil.format("&6" + player.getName() + "&8[&4" + profile.getLevel() + "&8] &7» &f") + event.getMessage());
                    } else {
                        Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
                        online.sendMessage(StringUtil.format("&7[&c" + clan.getName() + "&7 &6" + player.getName() + "&8[&4" + profile.getLevel() + "&8] &7» &f") + event.getMessage());
                    }
                }
            }
        });
    }
}
