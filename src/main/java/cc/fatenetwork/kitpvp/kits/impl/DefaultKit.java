package cc.fatenetwork.kitpvp.kits.impl;

import cc.fatenetwork.kitpvp.kits.Kits;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DefaultKit extends Kits {

    public DefaultKit(String name) {
        super(name);
    }

    @Override
    public String getName() {
        return "Default";
    }

    @Override
    public ItemStack[] getArmorContents() {
        List<ItemStack> toReturn = new ArrayList<>();
        ItemStack helm = new ItemStack(Material.GOLD_HELMET);
        ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemStack legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemStack boots = new ItemStack(Material.GOLD_BOOTS);
        toReturn.add(helm);
        toReturn.add(chest);
        toReturn.add(legs);
        toReturn.add(boots);
        return new ItemStack[]{
                boots,
                legs,
                chest,
                helm
        };
    }


    @Override
    public Inventory getInventory() {
        Inventory i = Bukkit.createInventory(null, 36);
        i.addItem(new ItemStack(Material.STONE_SWORD), new ItemStack(Material.FISHING_ROD), new ItemStack(Material.BOW), new ItemStack(Material.ARROW, 16), new ItemStack(Material.COOKED_BEEF, 8));
        return i;
    }
}
