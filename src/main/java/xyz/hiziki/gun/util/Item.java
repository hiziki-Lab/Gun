package xyz.hiziki.gun.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item
{
    public static ItemStack setMeta(Material item, String itemName)
    {
        ItemStack stack = new ItemStack(item);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(itemName);
        stack.setItemMeta(meta);
        return stack;
    }
}
