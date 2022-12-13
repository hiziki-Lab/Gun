package xyz.hiziki.gun.event.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity
{
    public EntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if (e.getEntity() instanceof Player p)
        {
            if (e.getDamager() instanceof Arrow arrow)
            {
                switch (arrow.getCustomName())
                {
                    case "AutomaticGun" -> e.setDamage(2);
                    case "ShotGun" -> e.setDamage(2.5);
                    case "SniperGun" -> e.setDamage(16);
                    case "ExplodingGun" -> Bukkit.getWorld("world").createExplosion(p.getLocation(), 4.0F);
                    case "HandGun" -> e.setDamage(1.5);
                }
            }
        }
    }
}
