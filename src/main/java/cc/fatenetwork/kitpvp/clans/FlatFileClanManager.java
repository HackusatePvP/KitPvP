package cc.fatenetwork.kitpvp.clans;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kbase.utils.chat.ClickAction;
import cc.fatenetwork.kbase.utils.chat.Text;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.spigotmc.CaseInsensitiveMap;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class FlatFileClanManager implements ClanManager, Listener {
    private final Map<String, Clan> clanNameMap = new CaseInsensitiveMap<>();
    private final Map<UUID, Clan> clanUUIDMap = new HashMap<>();
    private final Map<String, Clan> prefixClanMap = new CaseInsensitiveMap<>();
    private final KitPvP plugin;
    private List<Clan> clans = new ArrayList<>();

    public FlatFileClanManager(KitPvP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public List<Clan> getClans() {
        return this.clans;
    }


    @Override
    public Clan getClan(String var1) {
        return this.clanNameMap.get(var1);
    }

    @Override
    public Clan getClan(UUID var1) {
        return this.clanUUIDMap.get(var1);
    }

    @Override
    public boolean containClan(Clan var1) {
        return this.clans.contains(var1);
    }

    @Override
    public boolean inClan(Player var1) {
        Profile profile = plugin.getProfileManager().getProfile(var1.getUniqueId());
        return profile.isClan();
    }

    @Override
    public void createClan(Clan var1) {
        if (this.clans.add(var1)) {
            this.clanNameMap.put(var1.getName().toLowerCase(), var1);
            this.clanNameMap.put(var1.getPrefix().toLowerCase(), var1);
            this.clanUUIDMap.put(var1.getUuid(), var1);
        }
    }

    @Override
    public void removeClan(Clan var1) {
        if (this.clans.add(var1)) {
            this.clanNameMap.remove(var1.getName().toLowerCase(), var1);
            this.clanNameMap.remove(var1.getPrefix().toLowerCase(), var1);
            this.clanUUIDMap.remove(var1.getUuid(), var1);
        }
    }

    @Override
    public void addToClan(Player var1, Clan var2) {
        //todo create list
        if (var2 == null) {
            Bukkit.getLogger().info("Clan is null");
            return;
        }
        if (var2.getOnline() == null) {
            var2.setOnline(new ArrayList<>());
        }
        var2.getOnline().add(var1);
        var2.getMembers().add(var1.getName());
    }

    @Override
    public void removeFromClan(Player var1, Clan var2) {
        Profile profile = plugin.getProfileManager().getProfile(var1.getUniqueId());
        profile.setClanUUID(null);
        profile.setClanName("null");
        profile.setClan(false);
        var2.getMembers().remove(var1.getName());
    }

    @SneakyThrows
    @Override
    public void invitePlayer(Player var1, Player var2, Clan var3) {
        if (ClientAPI.isClient(var2)) {
            ClientAPI.sendNotification(var2, "You have been invited to a clan.", 5, TimeUnit.SECONDS);
        }
        Text text = new Text(var1.getName()).setColor(ChatColor.RED).append(new Text(" has invited you to ").setColor(ChatColor.GRAY));
        text.append(new Text(var3.getName() + " ").setColor(ChatColor.DARK_RED));
        text.append(new Text("Click here").setColor(ChatColor.BLUE).setUnderline(true).setClick(ClickAction.RUN_COMMAND, "" + "/clan join " + var3.getName()).setHoverText(ChatColor.translateAlternateColorCodes('&',"&7Click to join &6&lFate. ")).append())
                .append(new Text(" to accept this invitation").setColor(ChatColor.GREEN));
        text.send(var2);
    }

    @SneakyThrows
    @Override
    public void joinClan(Player var1, Clan var2) {
        Profile profile = plugin.getProfileManager().getProfile(var1.getUniqueId());
        profile.setClan(true);
        profile.setClanName(var2.getName());
        profile.setClanUUID(var2.getUuid());
        for (Player member : var2.getOnline()) {
            if (ClientAPI.isClient(member)) {
                ClientAPI.sendNotification(member, var1.getName() + " has joined the clan.", 3, TimeUnit.SECONDS);
            }
        }
    }

}
