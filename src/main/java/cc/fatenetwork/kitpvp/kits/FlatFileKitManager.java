package cc.fatenetwork.kitpvp.kits;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.kits.events.KitRenameEvent;
import cc.fatenetwork.kitpvp.utils.Config;
import cc.fatenetwork.kitpvp.utils.GenericUtils;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.ChatPaginator;

import java.util.*;

@Getter
public class FlatFileKitManager implements KitsManager, Listener {
    private static final int INV_WIDTH = 9;
    private final Map<String, Kit> kitNameMap = new CaseInsensitiveMap<String, Kit>();
    private final Map<UUID, Kit> kitUUIDMap = new HashMap<UUID, Kit>();
    private final KitPvP plugin;
    private Config config;
    private List<Kit> kits = new ArrayList<Kit>();

    public FlatFileKitManager(KitPvP plugin) {
        this.plugin = plugin;
        this.reloadKitData();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onKitRename(KitRenameEvent event){
        this.kitNameMap.remove(event.getOldName());
        this.kitNameMap.put(event.getNewName(), event.getKit());
    }
    @Override
    public List<Kit> getKits(){
        return this.kits;
    }

    @Override
    public Kit getKit(UUID uuid){
        return this.kitUUIDMap.get(uuid);
    }

    @Override
    public Kit getKit(String id){
        return this.kitNameMap.get(id);
    }

    @Override
    public boolean containsKit(Kit kit){
        return this.kits.contains(kit);
    }

    @Override
    public void createKit(Kit kit){
        if(this.kits.add(kit)){
            this.kitNameMap.put(kit.getName(), kit);
            this.kitUUIDMap.put(kit.getUuid(), kit);
        }
    }

    @Override
    public void removeKit(Kit kit){
        if(this.kits.remove(kit)){
            this.kitNameMap.remove(kit.getName());
            this.kitUUIDMap.remove(kit.getUuid());
        }
    }

    @Override
    public Inventory getGui(Player player){
        UUID uuid = player.getUniqueId();
        Inventory inventory = Bukkit.createInventory(player, (this.kits.size() + 9 - 1) / 9 * 9, ChatColor.BLUE + "Kit Selector");
        for(Kit kit : this.kits){
            ArrayList lore;
            ItemStack stack = kit.getImage();
            String description = kit.getDescription();
            String kitPermission = kit.getPermissionNode();
            if(kitPermission == null || player.hasPermission(kitPermission)){
                int maxUses;
                lore = new ArrayList();
                if(kit.isEnabled()){
                    if(kit.getDelay() > 0){
                        lore.add(ChatColor.YELLOW + kit.getDelayWords() + " cooldown");
                    }
                }else{
                    lore.add(ChatColor.RED + "Disabled");
                }
                if(description != null){
                    lore.add(" ");
                    for(String part : ChatPaginator.wordWrap(description, 24)){
                        lore.add(ChatColor.WHITE + part);
                    }
                }
            }else{
                lore = Lists.newArrayList((Object[]) new String[]{ChatColor.RED + "You do not own this kit."});
            }
            ItemStack cloned = stack.clone();
            ItemMeta meta = cloned.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + kit.getName());
            meta.setLore(lore);
            cloned.setItemMeta(meta);
            inventory.addItem(cloned);
        }
        return inventory;
    }

    @Override
    public void reloadKitData(){
        this.config = new Config(this.plugin, "kits");
        Object object = this.config.get("kits");
        if(object instanceof List){
            this.kits = GenericUtils.createList(object, Kit.class);
            for(Kit kit : this.kits){
                this.kitNameMap.put(kit.getName(), kit);
                this.kitUUIDMap.put(kit.getUuid(), kit);
            }
        }
    }

    @Override
    public void saveKitData(){
        this.config.set("kits", this.kits);
        this.config.save();
    }
}
