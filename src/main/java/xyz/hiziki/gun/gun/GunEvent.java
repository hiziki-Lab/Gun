package xyz.hiziki.gun.gun;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.util.Util;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class GunEvent
{
    private final JavaPlugin plugin = Main.getPlugin();

    private int count;

    public void automaticGun(Player shooter) //自動小銃
    {
        shooter.getWorld().playEffect(shooter.getLocation(), Effect.BOW_FIRE, 0);
        Vector vec = shooter.getEyeLocation().getDirection();
        Arrow arrow = shooter.getWorld().spawnArrow(shooter.getLocation().add(0, 1.75, 0), vec, 6F, 2F);
        arrow.setShooter(shooter);
        arrow.setGravity(true);
        arrow.setCustomName("automaticGun");

        shooter.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
    }

    public void shotGun(Player shooter) //散弾銃
    {
        shooter.getWorld().playEffect(shooter.getLocation(), Effect.BOW_FIRE, 0);
        Vector vec = shooter.getEyeLocation().getDirection();

        for (int i = 0; i < 16; i++)
        {
            Arrow arrow = shooter.getWorld().spawnArrow(shooter.getLocation().add(0, 1.75, 0), vec, 4F, 25F);
            arrow.setShooter(shooter);
            arrow.setGravity(false);
            arrow.setCustomName("shotGun");

            shooter.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
        }
    }

    public void sniperGun(Player shooter) //狙撃銃
    {
        count = 5;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (count == 0)
                {
                    shooter.getWorld().playEffect(shooter.getLocation(), Effect.BOW_FIRE, 0);
                    Vector vec = shooter.getEyeLocation().getDirection();
                    Arrow arrow = shooter.getWorld().spawnArrow(shooter.getLocation().add(0, 1.75, 0), vec, 10F, 0F);
                    arrow.setShooter(shooter);
                    arrow.setGravity(true);
                    arrow.setCustomName("sniperGun");

                    shooter.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
                    cancel();
                    return;
                }
                else if (count == 1)
                {
                    shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 5));
                }
                else if (count == 2)
                {
                    shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 4));
                }
                else if (count == 3)
                {
                    shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 3));
                }
                else if (count == 4)
                {
                    shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 2));
                }
                else if (count == 5)
                {
                    shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 1));
                }
                count--;
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    public void absorptionGun(Player shooter) //未定
    {
        shooter.getWorld().playEffect(shooter.getLocation(), Effect.BOW_FIRE, 0);

        Vector vec = shooter.getEyeLocation().getDirection();
        Arrow arrow = shooter.getWorld().spawnArrow(shooter.getLocation().add(0, 1.75, 0), vec, 4F, 2F);
        arrow.setShooter(shooter);
        arrow.setGravity(true);
        arrow.setCustomName("absorptionGun");

        shooter.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
    }

    public void flameThrowerGun(Player shooter) //火炎放射器
    {
        shooter.getWorld().playEffect(shooter.getLocation(), Effect.BLAZE_SHOOT, 0);
        Vector vec = shooter.getEyeLocation().getDirection();

        SmallFireball fireball = shooter.getWorld().spawn(shooter.getEyeLocation().add(0, 0.5, 0), SmallFireball.class);
        fireball.setShooter(shooter);
        fireball.setGravity(true);
        fireball.setVelocity(vec);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                SmallFireball fireball = shooter.getWorld().spawn(shooter.getEyeLocation().add(0, 0.5, 0), SmallFireball.class);
                fireball.setShooter(shooter);
                fireball.setGravity(false);
                fireball.setVelocity(vec);

                shooter.getServer().getScheduler().runTaskLater(plugin, fireball :: remove, 10);
            }
        }.runTaskLater(plugin, 3);

        shooter.getServer().getScheduler().runTaskLater(plugin, fireball :: remove, 10);
    }

    public void searchGun(Player shooter) //索敵銃
    {
        List<Block> blockList = shooter.getLineOfSight(null, 100);
        Location BlockLocation = blockList.get(blockList.size() - 1).getLocation();
        Location targetLocation = Util.under(BlockLocation);

        Predicate<Entity> Predicate = entity ->
        {
            if (entity.getType() == EntityType.PLAYER)
            {
                return !entity.getName().equals(shooter.getName());
            }
            return false;
        };

        Collection<Entity> targetList = targetLocation.getWorld().getNearbyEntities(targetLocation, 20, 20, 20, Predicate);
        shooter.getWorld().playEffect(targetLocation, Effect.DRAGON_BREATH, 10, 20);

        for (Entity entity : targetList)
        {
            for (int i = 0; i < 20; i++)
            {
                Player target = (Player) entity;

                target.addPotionEffect(PotionEffectType.GLOWING.createEffect(80, 1));
            }
        }
    }

    public void potionGun(Player shooter) //ポーション散布銃
    {
        shooter.getWorld().playEffect(shooter.getLocation(), Effect.BOW_FIRE, 0);
        Vector vec = shooter.getEyeLocation().getDirection();
        Arrow arrow = shooter.getWorld().spawnArrow(shooter.getLocation().add(0, 1.75, 0), vec, 2F, 2F);
        arrow.setShooter(shooter);
        arrow.setGravity(true);
        arrow.setCustomName("potionGun");

        shooter.getServer().getScheduler().runTaskTimer(plugin, () ->
        {
            if (!arrow.isDead())
            {
                ThrownPotion potion = (ThrownPotion) arrow.getWorld().spawnEntity(arrow.getLocation(), EntityType.SPLASH_POTION);
                ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
                PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 0), true);
                itemStack.setItemMeta(potionMeta);
                potion.setItem(itemStack);
                potion.setShooter(shooter);
            }
        }, 5, 5);

        shooter.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
    }

    public void handGun(Player shooter) //拳銃
    {
        shooter.getWorld().playEffect(shooter.getLocation(), Effect.BOW_FIRE, 0);
        Vector vec = shooter.getEyeLocation().getDirection();
        Arrow arrow = shooter.getWorld().spawnArrow(shooter.getLocation().add(0, 1.75, 0), vec, 3F, 2F);
        arrow.setShooter(shooter);
        arrow.setGravity(true);
        arrow.setCustomName("handGun");

        shooter.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
    }
}
