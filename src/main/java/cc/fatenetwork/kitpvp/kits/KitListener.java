package cc.fatenetwork.kitpvp.kits;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.utils.ParticleEffect;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class KitListener implements Listener {
    @Getter public static List<Inventory> previewInventory = new ArrayList<Inventory>();
    private final KitPvP plugin;

    public KitListener(KitPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if(previewInventory.contains(e.getInventory())){
            previewInventory.remove(e.getInventory());
            e.getInventory().clear();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event){
        if(previewInventory.contains(event.getInventory())){
            event.setCancelled(true);
            return;
        }
        Inventory inventory = event.getInventory();
        if(inventory == null){
            return;
        }
        String title = inventory.getTitle();
        HumanEntity humanEntity = event.getWhoClicked();
        if(title.contains("Kit Selector") && humanEntity instanceof Player){
            event.setCancelled(true);
            if(!Objects.equals(event.getView().getTopInventory(), event.getClickedInventory())){
                return;
            }
            ItemStack stack = event.getCurrentItem();
            if(stack == null || !stack.hasItemMeta()){
                return;
            }
            ItemMeta meta = stack.getItemMeta();
            if(!meta.hasDisplayName()){
                return;
            }
            Player player = (Player) humanEntity;
            String name = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
            Kit kit = plugin.getKitsManager().getKit(name);
            if(kit == null){
                return;
            }
            kit.applyTo(player, false, true);
            player.closeInventory();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onKitSign(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = event.getClickedBlock();
            BlockState state = block.getState();
            if(!(state instanceof Sign)){
                return;
            }
            Sign sign = (Sign) state;
            String[] lines = sign.getLines();
            if(lines.length >= 2 && lines[1].contains("Kit")){
                Kit kit = plugin.getKitsManager().getKit(lines.length >= 3 ? lines[2] : null);
                if(kit == null){
                    return;
                }
                event.setCancelled(true);
                Player player = event.getPlayer();
                String[] fakeLines = Arrays.copyOf(sign.getLines(), 4);
                boolean applied = kit.applyTo(player, false, false);
                if(applied){
                    fakeLines[0] = ChatColor.GREEN + "Successfully";
                    fakeLines[1] = ChatColor.GREEN + "equipped kit";
                    fakeLines[2] = kit.getDisplayName();
                    fakeLines[3] = "";
                }else{
                    fakeLines[0] = ChatColor.RED + "Failed to";
                    fakeLines[1] = ChatColor.RED + "equip kit";
                    fakeLines[2] = kit.getDisplayName();
                    fakeLines[3] = ChatColor.RED + "Check chat";
                }
                if(this.plugin.getSignHandler().showLines(player, sign, fakeLines, 100, false) && applied){
                    ParticleEffect.EXPLODE.display(player, sign.getLocation().clone().add(0.5, 0.5, 0.5), 0.01f, 10);
                }
            }
        }
    }

}
