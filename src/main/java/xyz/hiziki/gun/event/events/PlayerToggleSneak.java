package xyz.hiziki.gun.event.events;

import org.bukkit.event.player.PlayerToggleSneakEvent;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.gun.GunEnum;
import xyz.hiziki.gun.gun.GunInfoPlayer;
import xyz.hiziki.gun.util.GameGameMode;

public class PlayerToggleSneak
{
    public PlayerToggleSneak(PlayerToggleSneakEvent e)
    {
        GameGameMode gameMode = Main.getGameMode();

        if (gameMode != GameGameMode.NONE)
        {
            GunInfoPlayer target = Main.getPlayerGunInfo(e.getPlayer());
            GunEnum kind = GunEnum.getKind(e.getPlayer());

            if (kind != null)
            {
                //リロード処理
                target.reload(kind);
                //玉の残段数を表示
                target.viewBullet();
            }
        }
    }
}
