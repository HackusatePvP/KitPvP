package cc.fatenetwork.kitpvp.events;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.clans.Clan;
import cc.fatenetwork.kitpvp.clans.events.ClanMemberLoginEvent;
import cc.fatenetwork.kitpvp.kits.Kits;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import lombok.SneakyThrows;
import net.mineaus.lunar.api.LunarClientAPI;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
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
            player.getInventory().setArmorContents(Kits.DEFAULT_KIT.getArmorContents());
            player.getInventory().setContents(Kits.DEFAULT_KIT.getInventory().getContents());
            profile.getQuests().add("Starter Quest");
            profile.setActiveQuest("Starter Quest");
            player.sendMessage(StringUtil.format("&aProfile created."));
            player.setLevel(profile.getLevel());
            player.setExp((float) profile.getXp() / 100);
            Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new BukkitRunnable() {
                @SneakyThrows
                @Override
                public void run() {
                    if (ClientAPI.isClient(player)) {
                        ClientAPI.sendTitle(player, false, StringUtil.format("&7Welcome to &4&lKitPvP"), 1f, 3, 3, 3);
                        LunarClientAPI.INSTANCE().sendNotification(player, "You have gianed a new quest", 4);
                        ClientAPI.setWayPoint(player, "Spawn", new Location(Bukkit.getWorld("world"), 0 ,70 ,0), true);
                        LunarClientAPI.INSTANCE().updateServerName(player, "fatenetwork.cc");
                    }
                }
            }, 25L);
        } else {
            plugin.getMongoManager().getPlayer(player.getUniqueId()); //this will load the profile from the database
            player.sendMessage(StringUtil.format("&aProfile loaded.."));
            plugin.getLevelManager().updateLevel(profile);
            if (player.getInventory().getSize() == 0) {
                player.getInventory().setArmorContents(Kits.DEFAULT_KIT.getArmorContents());
                player.getInventory().setContents(Kits.DEFAULT_KIT.getInventory().getContents());
            }
            Bukkit.getOnlinePlayers().forEach(online -> {
                ClientAPI.updateNameTag(player, online, StringUtil.format(plugin.getRankManager().getRankPrefix(online) + " &f" + online.getName()));
                ClientAPI.updateNameTag(online, player, StringUtil.format(plugin.getRankManager().getRankPrefix(player) + " &f" + player.getName()));
            });
        }
        List<String> message = new ArrayList<>();
        message.add("&7&m-----------------------------------------");
        message.add("&7Welcome, &b" + player.getName() + " &7to &9KitPvP");
        message.add("&7Twitter: &9@FateKits");
        message.add("&7Website: &9www.fatenetwork.cc");
        message.add("&7&m-----------------------------------------");
        message.forEach(msg -> player.sendMessage(StringUtil.format(msg)));
        if (profile.isClan()) {
            //This means they are in a clan, clan could be disbanded
            if (plugin.getMongoManager().getClan(profile.getClanUUID()) == null) {
                //This means the clan uuid doesn ot exist in the database
                player.sendMessage(StringUtil.format("&cThe clan you were in was disbanded"));
                profile.setClan(false);
                profile.setClanUUID(null);
                profile.setClanName("null");
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

    @EventHandler
    public void onXp(ExpBottleEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity()).getPlayer();
            Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
            player.setExp((float) profile.getXp() / 100);
            player.setLevel(profile.getLevel());
        }
    }

    @EventHandler
    public void onInvenotryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            if (event.getPlayer() != null) {
                Player player = (Player) event.getPlayer();
                Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
                player.setLevel((int) profile.getBalance());
            }
        }
    }

    @EventHandler
    public void onAnvilUse(InventoryClickEvent event) {
        if (event.getWhoClicked() != null) {
            Player player = (Player) event.getWhoClicked();
            Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
            if (event.getInventory() != null) {
                if (event.getClickedInventory() instanceof AnvilInventory) {
                    ItemStack firstItem = event.getClickedInventory().getItem(0);
                    ItemStack secondItem = event.getClickedInventory().getItem(1);
                    ItemStack result = event.getClickedInventory().getItem(2);
                    if (firstItem != null && secondItem != null && result != null) {
                        if (firstItem.getType() != Material.AIR && secondItem.getType() != Material.AIR && result.getType() != Material.AIR) {
                            if (event.getCurrentItem().getItemMeta().getItemFlags() == result.getItemMeta().getItemFlags()) {
                                if (!(profile.getBalance() >= 15)) {
                                    player.sendMessage(StringUtil.format("&cYou do not have enough money to combine items."));
                                    player.closeInventory();
                                    player.getInventory().addItem(firstItem);
                                    player.getInventory().addItem(secondItem);
                                    return;
                                }
                                event.getClickedInventory().setItem(0, null);
                                event.getClickedInventory().setItem(1, null);
                                player.getInventory().addItem(result);
                                profile.setBalance(profile.getBalance() - 15);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event){
        ItemStack itemclicked = event.getCurrentItem();
        Player p = (Player) event.getWhoClicked();
        if(!(p instanceof Player))
            return;
        if (event.getInventory() instanceof EnchantingInventory) {
            if(itemclicked.getType().equals(Material.INK_SACK) && itemclicked.getDurability() == 4){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){
        if(e.getInventory() instanceof EnchantingInventory){
            EnchantingInventory inv = (EnchantingInventory) e.getInventory();
            Player player = (Player) e.getPlayer();
            Dye d = new Dye();
            d.setColor(DyeColor.BLUE);
            ItemStack i = d.toItemStack();
            i.setAmount(64);
            inv.setItem(1, i);
            player.updateInventory();
            List<String> message = new ArrayList<>();
            message.add("&7&m--------------------------------------");
            message.add("&4&lEnchanting &8Help");
            message.add("");
            message.add("&7The enchanter does not use your levels.");
            message.add("&7It will use your balance instead.");
            message.add("");
            message.add("&7&m--------------------------------------");
            message.forEach(msg -> player.sendMessage(StringUtil.format(msg)));
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() instanceof EnchantingInventory) {
            Dye d = new Dye();
            d.setColor(DyeColor.BLUE);
            ItemStack i = d.toItemStack();
            event.getInventory().contains(i);
            event.getInventory().remove(i);
            if (event.getPlayer() != null) {
                Player player = (Player) event.getPlayer();
                Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
                player.setLevel(profile.getLevel());
                player.setExp((float) profile.getXp() / 100);
            }
        }
        if (event.getInventory() instanceof AnvilInventory) {
            if (event.getPlayer() != null) {
                Player player = (Player) event.getPlayer();
                Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
                player.setLevel(profile.getLevel());
                player.setExp((float) profile.getXp() / 100);
            }
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        profile.setBalance(profile.getBalance() - event.getExpLevelCost());
        player.setExp((float) profile.getBalance() / 100);
    }

    @EventHandler
    public void onFireSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

}
