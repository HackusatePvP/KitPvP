package cc.fatenetwork.kitpvp.kits;

import cc.fatenetwork.kitpvp.kits.impl.DefaultKit;
import cc.fatenetwork.kitpvp.kits.impl.MasterKit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Kits {
    private static Map<String, Kits> byName = new HashMap<>();

    public static Kits DEFAULT_KIT = new DefaultKit("Default");
    public static Kits MASTER_KIT = new MasterKit("Master");

    public Kits(String name) {
        byName.put(name, this);
    }

    public static Kits getByName(String name) {
        return byName.get(name);
    }

    public abstract String getName();

    public abstract ItemStack[] getArmorContents();

    public abstract Inventory getInventory();

}
