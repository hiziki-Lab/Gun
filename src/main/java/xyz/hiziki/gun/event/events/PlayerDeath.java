package xyz.hiziki.gun.event.events;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.util.GunGameMode;

public class PlayerDeath
{
    public PlayerDeath(PlayerDeathEvent e)
    {
        if (Main.getGameMode() != GunGameMode.NONE)
        {
            Player player = e.getEntity();
            Player killer = player.getKiller();

            if (killer != null)
            {
                if (killer.getType() == EntityType.PLAYER)
                {
                    switch (Main.getGameMode())
                    {
                        case TEAM -> Main.getScoreBoard().pointCheck(player.getKiller().getPlayer(), player);
                        case ONE_LIFE -> player.setGameMode(GameMode.SPECTATOR);
                    }
                }
            }
        }
    }
}