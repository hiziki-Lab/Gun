package xyz.hiziki.gun.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    public static ItemStack itemMeta(Material item, String itemName)
    {
        ItemStack stack = new ItemStack(item);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(itemName);
        stack.setItemMeta(meta);
        return stack;
    }
}
