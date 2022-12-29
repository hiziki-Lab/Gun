package xyz.hiziki.gun.util;

import org.bukkit.Location;
import org.bukkit.Material;

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
}
