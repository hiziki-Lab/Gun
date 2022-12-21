package xyz.hiziki.gun.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Util
{
    public static Location under(Location loc)
    {
        if (loc.getBlock().getType() != Material.AIR)
        {
            return loc;
        }

        while (loc.getBlock().getType() == Material.AIR)
        {
            loc.add(0, -1, 0);
        }
        return loc;
    }

    public static ItemStack itemMeta(Material item, String itemName, String lore1, String lore2, String lore3)
    {
        ItemStack stack = new ItemStack(item);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(itemName);
        meta.setLore(Arrays.asList(lore1, lore2, lore3));
        stack.setItemMeta(meta);
        return stack;
    }
}
