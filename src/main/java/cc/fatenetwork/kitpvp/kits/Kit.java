package cc.fatenetwork.kitpvp.kits;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.kits.events.KitApplyEvent;
import cc.fatenetwork.kitpvp.utils.GenericUtils;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import com.google.common.base.Preconditions;
import lombok.Data;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;

import java.util.*;

@Data
public class Kit implements ConfigurationSerializable {
    private static final ItemStack DEFAULT_IMAGE = new ItemStack(Material.EMERALD, 1);
    private UUID uuid;
    private String name, displayName, description;
    private long delay;
    private ItemStack[] items;
    private ItemStack[] armor;
    private ItemStack displayItem;
    private boolean enabled;


    public Kit(String name, String description, PlayerInventory inventory){
        this(name, description, inventory, 0);
    }

    public Kit(String name, String description, Inventory inventory, long milliseconds){
        this.enabled = true;
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.setItems(inventory.getContents());
        if(inventory instanceof PlayerInventory){
            PlayerInventory playerInventory = (PlayerInventory) inventory;
            this.setArmour(playerInventory.getArmorContents());
            this.setImage(playerInventory.getItemInHand());
        }
    }

    public Kit(final Map<String, Object> map){
        this.uuid = UUID.fromString((String) map.get("uniqueID"));
        this.setName((String) map.get("name"));
        this.setDescription((String) map.get("description"));
        this.setEnabled((Boolean) map.get("enabled"));
        final List<ItemStack> items = GenericUtils.createList(map.get("items"), ItemStack.class);
        this.setItems(items.toArray(new ItemStack[items.size()]));
        final List<ItemStack> armour = GenericUtils.createList(map.get("armour"), ItemStack.class);
        this.setArmour(armour.toArray(new ItemStack[armour.size()]));
        this.setImage((ItemStack) map.get("image"));
        this.setDelayMillis(Long.parseLong((String) map.get("delay")));
    }

    public Map<String, Object> serialize(){
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("uniqueID", this.uuid.toString());
        map.put("name", this.name);
        map.put("description", this.description);
        map.put("enabled", this.enabled);
        map.put("items", this.items);
        map.put("armour", this.armor);
        map.put("image", this.getImage());
        map.put("delay", Long.toString(this.delay));
        return map;
    }


    public void setItems(ItemStack[] items) {
        int length = items.length;
        this.items = new ItemStack[length];
        for (int i = 0; i< length; ++i) {
            ItemStack next = items[i];
            this.items[i] = next == null ? null : next.clone();
        }
    }

    public Inventory getPreview(Player player) {
        Inventory i = Bukkit.createInventory(null, 9, StringUtil.format("&a&l" + this.name + " &7Preview"));
        for (ItemStack itemStack : items) {
            i.addItem(itemStack);
        }
        return i;
    }

    public ItemStack[] getItems(){
        return Arrays.copyOf(this.items, this.items.length);
    }

    public void setArmour(ItemStack[] armour){
        int length = armour.length;
        this.armor = new ItemStack[length];
        for(int i = 0; i < length; ++i){
            ItemStack next = armour[i];
            this.armor[i] = next == null ? null : next.clone();
        }
    }

    public ItemStack getImage(){
        if(this.displayItem == null || this.displayItem.getType() == Material.AIR){
            this.displayItem = DEFAULT_IMAGE;
        }
        return this.displayItem;
    }

    public void setImage(ItemStack image){
        this.displayItem = image == null || image.getType() == Material.AIR ? null : image.clone();
    }

    public String getDelayWords(){
        return DurationFormatUtils.formatDurationWords(this.delay, true, true);
    }

    public void setDelayMillis(long delayMillis){
        if(this.delay != delayMillis){
            this.delay = delayMillis;
        }
    }

    public String getPermissionNode(){
        return "kits.kit." + this.name;
    }

    public Permission getBukkitPermission(){
        String node = this.getPermissionNode();
        return node == null ? null : new Permission(node);
    }

    public boolean applyTo(Player player, boolean force, boolean inform){
        KitApplyEvent event = new KitApplyEvent(this, player, force);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return false;
        }
        if(KitPvP.getPlugin().getConfig().getBoolean("kit-clear")){
            PlayerInventory targetInventory = player.getInventory();
            targetInventory.clear();
            targetInventory.setArmorContents(new ItemStack[]{new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1)});
        }
        ItemStack cursor = player.getItemOnCursor();
        Location location = player.getLocation();
        World world = player.getWorld();
        if(cursor != null && cursor.getType() != Material.AIR){
            player.setItemOnCursor(new ItemStack(Material.AIR, 1));
            world.dropItemNaturally(location, cursor);
        }
        PlayerInventory inventory = player.getInventory();
        for(ItemStack item : this.items){
            if(item == null || item.getType() == Material.AIR) continue;
            item = item.clone();
            for(Map.Entry excess : inventory.addItem(new ItemStack[]{item.clone()}).entrySet()){
                world.dropItemNaturally(location, (ItemStack) excess.getValue());
            }
        }
        if(this.armor != null){
            for(int i = Math.min(3, this.armor.length); i >= 0; --i){
                ItemStack stack = this.armor[i];
                if(stack == null || stack.getType() == Material.AIR) continue;
                int armourSlot = i + 36;
                ItemStack previous = inventory.getItem(armourSlot);
                stack = stack.clone();
                if(previous != null && previous.getType() != Material.AIR){
                    previous.setType(Material.AIR);
                    world.dropItemNaturally(location, stack);
                    continue;
                }
                inventory.setItem(armourSlot, stack);
            }
        }
        if(inform){
            player.sendMessage(ChatColor.YELLOW + "Kit " + ChatColor.AQUA + this.name + ChatColor.YELLOW + " has been applied.");
        }
        return true;
    }

}
