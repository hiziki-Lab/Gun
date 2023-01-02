package xyz.hiziki.gun.event.events;

import org.bukkit.event.player.PlayerInteractEvent;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.gun.GunEnum;
import xyz.hiziki.gun.gun.GunInfoPlayer;
import xyz.hiziki.gun.util.GunGameMode;

public class PlayerInteract
{
    public PlayerInteract(PlayerInteractEvent e)
    {
        if (Main.getGameMode() != GunGameMode.NONE)
        {
            GunInfoPlayer target = Main.getPlayerGunInfo(e.getPlayer());
            GunEnum kind = GunEnum.getKind(e.getPlayer());

            if (kind != null)
            {
                target.fire(kind);//射撃
                target.viewBullet();//玉の残段数を表示
            }
        }
    }
}
