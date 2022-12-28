package xyz.hiziki.gun.guns;

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

public class GunItem
{
    private final JavaPlugin plugin = Main.getPlugin();

    public void automaticGun(Player p)
    {
        p.getWorld().playEffect(p.getLocation(), Effect.BOW_FIRE, 0);
        Vector vec = p.getEyeLocation().getDirection();
        Arrow arrow = p.getWorld().spawnArrow(p.getLocation().add(0, 1.75, 0), vec, 6F, 2F);
        arrow.setShooter(p);
        arrow.setGravity(true);
        arrow.setCustomName("AutomaticGun");

        p.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
    }

    public void shotGun(Player p) //散弾銃
    {
        p.getWorld().playEffect(p.getLocation(), Effect.BOW_FIRE, 0);
        Vector vec = p.getEyeLocation().getDirection();

        for (int i = 0; i < 16; i++)
        {
            Arrow arrow = p.getWorld().spawnArrow(p.getLocation().add(0, 1.75, 0), vec, 2F, 25F);
            arrow.setShooter(p);
            arrow.setGravity(false);
            arrow.setCustomName("ShotGun");

            p.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
        }
    }

    private int count;

    public void sniperGun(Player p)
    {
        count = 5;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (count == 0)
                {
                    p.getWorld().playEffect(p.getLocation(), Effect.BOW_FIRE, 0);
                    Vector vec = p.getEyeLocation().getDirection();
                    Arrow arrow = p.getWorld().spawnArrow(p.getLocation().add(0, 1.75, 0), vec, 10F, 0F);
                    arrow.setShooter(p);
                    arrow.setGravity(true);
                    arrow.setCustomName("SniperGun");

                    p.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
                    cancel();
                    return;
                }
                else if (count == 1)
                {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 5));
                }
                else if (count == 2)
                {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 4));
                }
                else if (count == 3)
                {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3));
                }
                else if (count == 4)
                {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 2));
                }
                else if (count == 5)
                {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
                }
                count--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void absorptionGun(Player p)
    {
        p.getWorld().playEffect(p.getLocation(), Effect.BOW_FIRE, 0);

        Vector vec = p.getEyeLocation().getDirection();
        Arrow arrow = p.getWorld().spawnArrow(p.getLocation().add(0, 1.75, 0), vec, 4F, 2F);
        arrow.setShooter(p);
        arrow.setGravity(true);
        arrow.setCustomName("absorptionGun");

        p.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
    }

    public void flameThrowerGun(Player p) //火炎放射器
    {
        p.getWorld().playEffect(p.getLocation(), Effect.BLAZE_SHOOT, 0);
        Vector vec = p.getEyeLocation().getDirection();

        SmallFireball fireball = p.getWorld().spawn(p.getEyeLocation().add(0, 0.5, 0), SmallFireball.class);
        fireball.setShooter(p);
        fireball.setGravity(true);
        fireball.setVelocity(vec);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                SmallFireball fireball = p.getWorld().spawn(p.getEyeLocation().add(0, 0.5, 0), SmallFireball.class);
                fireball.setShooter(p);
                fireball.setGravity(false);
                fireball.setVelocity(vec);

                p.getServer().getScheduler().runTaskLater(plugin, fireball :: remove, 10);
            }
        }.runTaskLater(plugin, 3);

        p.getServer().getScheduler().runTaskLater(plugin, fireball :: remove, 10);
    }

    public void searchGun(Player p)
    {
        List<Block> blockList = p.getLineOfSight(null, 100);
        Location BlockLocation = blockList.get(blockList.size() - 1).getLocation();
        Location targetLocation = Util.under(BlockLocation);

        Predicate<Entity> Predicate = entity ->
        {
            if (entity.getType() == EntityType.PLAYER)
            {
                return !entity.getName().equals(p.getName());
            }
            return false;
        };

        Collection<Entity> targetList = targetLocation.getWorld().getNearbyEntities(targetLocation, 20, 20, 20, Predicate);
        p.getWorld().playEffect(targetLocation, Effect.DRAGON_BREATH, 10, 20);

        for (Entity entity : targetList)
        {
            for (int i = 0; i < 20; i++)
            {
                Player target = (Player) entity;

                target.addPotionEffect(PotionEffectType.GLOWING.createEffect(80, 1));
            }
        }
    }

    public void potionGun(Player p) //ポーション散布銃
    {
        p.getWorld().playEffect(p.getLocation(), Effect.BOW_FIRE, 0);
        Vector vec = p.getEyeLocation().getDirection();
        Arrow arrow = p.getWorld().spawnArrow(p.getLocation().add(0, 1.75, 0), vec, 1.5F, 2F);
        arrow.setShooter(p);
        arrow.setGravity(true);
        arrow.setCustomName("PotionGun");

        p.getServer().getScheduler().runTaskTimer(plugin, () ->
        {
            if (!arrow.isDead())
            {
                ThrownPotion potion = (ThrownPotion) arrow.getWorld().spawnEntity(arrow.getLocation(), EntityType.SPLASH_POTION);
                ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
                PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 0), true);
                itemStack.setItemMeta(potionMeta);
                potion.setItem(itemStack);
                potion.setShooter(p);
            }
        }, 5, 5);

        p.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
    }

    public void handGun(Player p)
    {
        p.getWorld().playEffect(p.getLocation(), Effect.BOW_FIRE, 0);
        Vector vec = p.getEyeLocation().getDirection();
        Arrow arrow = p.getWorld().spawnArrow(p.getLocation().add(0, 1.75, 0), vec, 3F, 2F);
        arrow.setShooter(p);
        arrow.setGravity(true);
        arrow.setCustomName("HandGun");

        p.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 200);
    }
}
