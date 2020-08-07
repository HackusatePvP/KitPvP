package cc.fatenetwork.kitpvp.kits.impl;

import cc.fatenetwork.kitpvp.kits.Kits;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MasterKit extends Kits {

    public MasterKit(String name) {
        super(name);
    }

    @Override
    public String getName() {
        return "Master";
    }

    @Override
    public ItemStack[] getArmorContents() {
        ItemStack helm = new ItemStack(Material.IRON_HELMET);
        ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemStack legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemStack boots = new ItemStack(Material.IRON_HELMET);
        helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
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
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        i.addItem(sword, new ItemStack(Material.GOLDEN_APPLE, 1),  new ItemStack(Material.FISHING_ROD), bow);
        return i;
    }
}
