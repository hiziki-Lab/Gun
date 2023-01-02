package xyz.hiziki.gun.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.hiziki.gun.Main;

public class RemoveGun
{
    public RemoveGun()
    {
        JavaPlugin plugin = Main.getPlugin();

        for (Player target : plugin.getServer().getOnlinePlayers())
        {
            for (ItemStack item : target.getInventory())
            {
                if (item != null)
                {
                    switch (item.getType())
                    {
                        case MUSIC_DISC_13, MUSIC_DISC_CAT, MUSIC_DISC_BLOCKS, MUSIC_DISC_CHIRP,
                                MUSIC_DISC_FAR, MUSIC_DISC_MELLOHI, MUSIC_DISC_STAL, MUSIC_DISC_STRAD
                                -> target.getInventory().removeItem(item);
                    }
                }
            }
        }
    }
}
