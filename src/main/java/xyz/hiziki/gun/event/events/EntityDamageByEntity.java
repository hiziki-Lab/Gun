package xyz.hiziki.gun.event.events;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.hiziki.gun.Main;

public class EntityDamageByEntity
{
    public EntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        JavaPlugin plugin = Main.getPlugin();

        if (e.getEntity() instanceof LivingEntity p)
        {
            if (e.getDamager() instanceof Arrow arrow)
            {
                if (arrow.getCustomName() != null)
                {
                    switch (arrow.getCustomName())
                    {
                        case "AutomaticGun", "ShotGun" -> e.setDamage(2);
                        case "SniperGun" -> e.setDamage(16);
                        case "AbsorptionGun" ->
                        {
                            e.setDamage(2);
                            Player shooter = (Player) e.getDamager();
                            double health = shooter.getHealth();
                            shooter.setHealth(health + 1);
                        }
                        case "PotionGun" ->
                        {
                            e.setDamage(0);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 5));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 5));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 5));
                        }
                        case "HandGun" -> e.setDamage(1.5);
                    }


                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            p.setNoDamageTicks(0);
                        }
                    }.runTaskLaterAsynchronously(plugin, 2L);
                }
            }
        }
    }
}
