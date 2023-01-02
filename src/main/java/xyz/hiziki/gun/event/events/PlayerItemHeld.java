package xyz.hiziki.gun.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.gun.GunInfoPlayer;
import xyz.hiziki.gun.util.GunGameMode;

public class PlayerItemHeld
{
    public PlayerItemHeld(PlayerItemHeldEvent e)
    {
        if (Main.getGameMode() != GunGameMode.NONE)
        {
            changeGun(e.getPlayer());
        }
    }

    private void changeGun(Player player)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                GunInfoPlayer target = Main.getPlayerGunInfo(player);
                target.viewBullet(); //玉の残段数を表示
            }
        }.runTaskLater(Main.getPlugin(), 1);
    }
}
