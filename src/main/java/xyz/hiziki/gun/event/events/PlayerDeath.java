package xyz.hiziki.gun.event.events;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.util.GameRole;

import java.util.Random;

public class PlayerDeath
{
    private int count;

    public PlayerDeath(PlayerDeathEvent e)
    {
        JavaPlugin plugin = Main.getPlugin();

        Player player = e.getEntity();
        Entity en = player.getKiller();

        if (en instanceof Player killer)
        {
            GameRole playerRole = Main.getPlayerRole().get(killer);

            if (playerRole == GameRole.DESTROYER)
            {
                Random random = new Random();
                int num = random.nextInt(5) + 1;

                switch (num)
                {
                    case 1 -> killer.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 100));
                    case 2 -> killer.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1, 100));
                    case 3 -> killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, 100));
                    case 4 -> killer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1, 100));
                    case 5 -> killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 100));
                }

                count = 5;

                Location loc = killer.getLocation();

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (count == 0)
                        {
                            cancel();
                            return;
                        }

                        player.spawnParticle(Particle.DRIP_LAVA, loc, 5);
                        count--;
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
        }
    }
}
