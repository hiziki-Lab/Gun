package xyz.hiziki.gun.guns;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
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
    public static void SetGun(Player player)
    {
        Inventory inv = player.getInventory();

        for (GunItemEnum gunItem : GunItemEnum.values())
        {
            inv.setItem(gunItem.ordinal(), gunItem.getGunItemStack());
        }
    }

    public static class Info
    {
        static ItemStack AutomaticGun() //自動小銃
        {
            return Util.itemMeta(Material.MUSIC_DISC_13, ChatColor.GOLD + "自動小銃",
                    "まゆマークが", "後ろに", "ついている");
        }

        static ItemStack ShotGun() //散弾銃
        {
            return Util.itemMeta(Material.MUSIC_DISC_CAT, ChatColor.GOLD + "散弾銃",
                    "真ん中の", "まゆさんマークが", "柔らかい");
        }

        static ItemStack FlameThrowerGun() //放射器
        {
            return Util.itemMeta(Material.MUSIC_DISC_FAR, ChatColor.GOLD + "火炎放射器",
                    "炎がでてくる", "でもよくみると", "赤いきのこ");
        }

        static ItemStack SearchGun() //索敵銃
        {
            return Util.itemMeta(Material.MUSIC_DISC_MELLOHI, ChatColor.GOLD + "索敵銃",
                    "アンテナの", "代わりに", "まゆさんヘッド");
        }

        static ItemStack PotionGun() //散布銃
        {
            return Util.itemMeta(Material.MUSIC_DISC_STAL, ChatColor.GOLD + "ポーション散布銃",
                    "一番", "弱い", "銃かもしれない");
        }
    }

    public static class Event
    {
        public static void AutomaticGunRod(Player player)
        {
            JavaPlugin plugin = Main.getPlugin();

            player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 0);
            Vector vec = player.getEyeLocation().getDirection();
            Arrow arrow = player.getWorld().spawnArrow(player.getLocation().add(0, 2, 0), vec, 2F, 2F);
            arrow.setShooter(player);
            arrow.setColor(Color.fromBGR((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            arrow.setGravity(false);
            player.getServer().getScheduler().runTaskLater(plugin, arrow::remove, 10 * 20);
        }

        public static void ShotGunRod(Player player) //散弾銃
        {
            JavaPlugin plugin = Main.getPlugin();

            Vector vec = player.getEyeLocation().getDirection();

            for (int shotNo = 0; shotNo < 10; shotNo++)
            {
                Arrow arrow = player.getWorld().spawnArrow(player.getLocation().add(0, 2, 0), vec, 3F, 25F);
                arrow.setShooter(player);

                arrow.setColor(Color.fromBGR((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
                arrow.setGravity(false);
                player.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 20);
            }
        }

        public static void FlameThrowerGun(Player player) //火炎放射器
        {
            JavaPlugin plugin = Main.getPlugin();

            player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
            Vector vec = player.getEyeLocation().getDirection();

            SmallFireball smallFireball = player.getWorld().spawn(player.getEyeLocation().add(0, 0.5, 0), SmallFireball.class);
            smallFireball.setShooter(player);
            smallFireball.setGravity(true);
            smallFireball.setVelocity(vec);

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    SmallFireball smallFireball = player.getWorld().spawn(player.getEyeLocation().add(0, 0.5, 0), SmallFireball.class);
                    smallFireball.setShooter(player);
                    smallFireball.setGravity(true);
                    smallFireball.setVelocity(vec);

                    player.getServer().getScheduler().runTaskLater(plugin, smallFireball :: remove, 10);
                }
            }.runTaskLater(plugin, 3);


            player.getServer().getScheduler().runTaskLater(plugin, smallFireball :: remove, 10);
        }

        public static void SearchGunRod(Player player)
        {
            List<Block> blockList = player.getLineOfSight(null, 50);
            Location BlockLocation = blockList.get(blockList.size() - 1).getLocation();
            Location targetLocation = Util.under(BlockLocation);

            Predicate<Entity> Predicate = entity ->
            {
                if (entity.getType() == EntityType.PLAYER)
                {
                    return !entity.getName().equals(player.getName());
                }
                return false;
            };

            Collection<Entity> targetList = targetLocation.getWorld().getNearbyEntities(targetLocation,
                    20, 20, 20, Predicate);
            player.getWorld().playEffect(targetLocation, Effect.DRAGON_BREATH, 10, 20);

            for (Entity entity : targetList)
            {
                for (int i = 0; i < 20; i++)
                {
                    Player target = (Player) entity;

                    target.addPotionEffect(PotionEffectType.GLOWING.createEffect(80, 1));
                }
            }
        }

        public static void potionGunRos(Player player) //ポーション散布銃
        {
            JavaPlugin plugin = Main.getPlugin();

            player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 0);
            Vector vec = player.getEyeLocation().getDirection();
            Arrow arrow = player.getWorld().spawnArrow(player.getLocation().add(0, 2, 0), vec, 0.3F, 8F);
            arrow.setShooter(player);
            arrow.setColor(Color.fromBGR((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            arrow.setGravity(false);

            player.getServer().getScheduler().runTaskTimer(plugin, () ->
            {
                if (!arrow.isDead())
                {
                    ThrownPotion potion = (ThrownPotion) arrow.getWorld().spawnEntity(arrow.getLocation(), EntityType.SPLASH_POTION);
                    ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
                    PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 20 * 8, 0), true);
                    itemStack.setItemMeta(potionMeta);
                    potion.setItem(itemStack);
                    potion.setShooter(player);
                    arrow.setColor(Color.fromBGR((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
                }
            }, 10, 20);
            player.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 10 * 20);
        }
    }
}
