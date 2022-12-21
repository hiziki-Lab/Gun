package xyz.hiziki.gun.event.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityDamageByEntity
{
    public EntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if (e.getEntity() instanceof Player p)
        {
            if (e.getDamager() instanceof Arrow arrow)
            {
                if (arrow.getCustomName() != null)
                {
                    switch (arrow.getCustomName())
                    {
                        case "AutomaticGun", "ShotGun" -> e.setDamage(2);
                        case "SniperGun" -> e.setDamage(16);
                        case "ExplodingGun" -> Bukkit.getWorld("world").createExplosion(p.getLocation(), 2.0F);
                        case "PotionGun" -> p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2));
                        case "HandGun" -> e.setDamage(1.5);
                    }
                }
            }
        }
    }
}
