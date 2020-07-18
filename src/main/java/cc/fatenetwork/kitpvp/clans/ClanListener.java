package cc.fatenetwork.kitpvp.clans;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.clans.events.*;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ClanListener implements Listener {
    private final KitPvP plugin;

    public ClanListener(KitPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClanCreate(ClanCreateEvent event) {
        Player player = event.getPlayer();
        if (plugin.getCombatManager().isCombat(player)) {
            player.sendMessage(StringUtil.format("&cYou cannot create a clan whilst in combat."));
            event.setCancelled(true);
            return;
        }
        if (plugin.getClanManager().getClan(event.getName().toLowerCase()) != null) {
            player.sendMessage(StringUtil.format("&cClan name already exists."));
            event.setCancelled(true);
            return;
        }
        if (plugin.getClanManager().getClan(event.getPrefix().toLowerCase()) != null) {
            player.sendMessage(StringUtil.format("&cClan prefix already exists."));
            event.setCancelled(true);
            return;
        }
        Bukkit.getOnlinePlayers().forEach(online -> online.sendMessage(StringUtil.format("&c" + player.getName() + " &7has created the clan &4" + event.getName())));
    }

    @EventHandler
    public void onClanLeave(ClanLeaveEvent event) {
        Clan clan = event.getClan();
        Player player = event.getPlayer();
        if (clan.getLeader() == player.getUniqueId()) {
            player.sendMessage(StringUtil.format("&cYou cannot leave a clan you created. /clan disband."));
            event.setCancelled(true);
        }
        if (plugin.getCombatManager().isCombat(player)) {
            player.sendMessage(StringUtil.format("&cYou cannot leave a clan whilst in combat."));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClanInvite(ClanInviteEvent event) {
        Player player = event.getPlayer();
        Player invited = event.getInvited();
        if (plugin.getCombatManager().isCombat(player)) {
            player.sendMessage(StringUtil.format("&cYou cannot invite players whilst in combat."));
            event.setCancelled(true);
        }
        if (plugin.getClanManager().inClan(invited)) {
            player.sendMessage(StringUtil.format("&cPlayer is already in a clan."));
            event.setCancelled(true);
        }
        /*if (plugin.getClanManager().invited().containsKey(invited)) {
            player.sendMessage(StringUtil.format("&cTarget was already invited to a clan."));
            event.setCancelled(true);
            return;
        } */
    }

    @EventHandler
    public void onClanMemberJoinEvent(ClanMemberLoginEvent event) {
        Player player = event.getPlayer();
        Clan clan = event.getClan();
        Map<UUID, Map<String, Double>> players = new HashMap<>();
        Map<String, Double> posMap = new HashMap<>();
        posMap.put("a", 0.0);
        if (clan.getOnline() == null) {
            return;
        }
        clan.getOnline().forEach(member -> {
            try {
                ClientAPI.sendTeamMate(player, member);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clan.getOnline().add(player);
    }

    @EventHandler
    public void onClanJoin(ClanJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getCombatManager().isCombat(player)) {
            player.sendMessage(StringUtil.format("&cYou cannot join a clan whilst in combat."));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
            if (!profile.isClan()) {
                return;
            }
            Profile tar = plugin.getProfileManager().getProfile(damager.getUniqueId());
            if (!tar.isClan()) {
                return;
            }
            Clan clan = plugin.getClanManager().getClan(profile.getClanUUID());
            if (clan == null) {
                return;
            }
            Clan tarc = plugin.getClanManager().getClan(tar.getClanUUID());
            if (tarc == null) {
                return;
            }
            if (tarc.getName().equalsIgnoreCase(clan.getName())) {
                ClanFriendlyFireEvent e = new ClanFriendlyFireEvent(player, damager, clan);
                if (e.isCancelled()) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
