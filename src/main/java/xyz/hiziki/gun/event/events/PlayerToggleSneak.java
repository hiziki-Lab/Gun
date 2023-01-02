package xyz.hiziki.gun.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.gun.GunEnum;
import xyz.hiziki.gun.gun.GunInfoPlayer;
import xyz.hiziki.gun.util.GunGameMode;

public class PlayerToggleSneak
{
    public PlayerToggleSneak(PlayerToggleSneakEvent e)
    {
        if (Main.getGameMode() != GunGameMode.NONE)
        {
            gunReload(e.getPlayer());
        }
    }

    private void gunReload(Player player)
    {
        GunInfoPlayer target = Main.getPlayerGunInfo(player);
        GunEnum kind = GunEnum.getKind(player);

        if (kind != null)
        {
            //リロード処理
            target.reload(kind);
            //玉の残段数を表示
            target.viewBullet();
        }
    }
}
