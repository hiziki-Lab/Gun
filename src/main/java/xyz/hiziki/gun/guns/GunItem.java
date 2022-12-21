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
    public static void setGun(Player player)
    {
        Inventory inv = player.getInventory();

        for (GunItemEnum gunItem : GunItemEnum.values())
        {
            inv.setItem(gunItem.ordinal(), gunItem.getGunItemStack());
        }
    }

    public static class Info
    {
        public static ItemStack automaticGun() //自小銃
        {
            return Util.itemMeta(Material.MUSIC_DISC_13, ChatColor.GOLD + "自動小銃",
                    "まゆマークが", "後ろに", "ついている");
        }

        public static ItemStack shotGun() //散弾銃
        {
            return Util.itemMeta(Material.MUSIC_DISC_CAT, ChatColor.GOLD + "散弾銃",
                    "真ん中の", "まゆさんマークが", "柔らかい");
        }

        public static ItemStack sniperGun() //狙撃銃
        {
            return Util.itemMeta(Material.MUSIC_DISC_BLOCKS, ChatColor.GOLD + "狙撃銃",
                    "放たれる弾", "とてつもない早い", "キノコの胞子");
        }

        public static ItemStack explodingGun() //爆裂銃
        {
            return Util.itemMeta(Material.MUSIC_DISC_CHIRP, ChatColor.GOLD + "爆裂銃(仮)",
                    "この矢には", "世界を滅ぼす", "力がある");
        }

        public static ItemStack flameThrowerGun() //放射器
        {
            return Util.itemMeta(Material.MUSIC_DISC_FAR, ChatColor.GOLD + "火炎放射器",
                    "炎がでてくる", "でもよくみると", "赤いきのこ");
        }

        public static ItemStack searchGun() //索敵銃
        {
            return Util.itemMeta(Material.MUSIC_DISC_MELLOHI, ChatColor.GOLD + "索敵銃",
                    "アンテナの", "代わりに", "まゆさんヘッド");
        }

        public static ItemStack potionGun() //散布銃
        {
            return Util.itemMeta(Material.MUSIC_DISC_STAL, ChatColor.GOLD + "ポーション散布銃",
                    "一番", "強い", "銃かもしれない");
        }

        public static ItemStack handGun() //小拳銃
        {
            return Util.itemMeta(Material.MUSIC_DISC_STRAD, ChatColor.GOLD + "拳銃",
                    "銃ですら", "ないかも", "しれない");
        }
    }

    public static class Event
    {
        private final static JavaPlugin plugin = Main.getPlugin();

        public static void automaticGun(Player p)
        {
            p.getWorld().playEffect(p.getLocation(), Effect.BOW_FIRE, 0);
            Vector vec = p.getEyeLocation().getDirection();
            Arrow arrow = p.getWorld().spawnArrow(p.getLocation().add(0, 2, 0), vec, 6F, 2F);
            arrow.setShooter(p);
            arrow.setColor(Color.fromBGR((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            arrow.setGravity(true);
            arrow.setCustomName("AutomaticGun");

            p.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 10 * 20);
        }

        public static void shotGun(Player p) //散弾銃
        {
            p.getWorld().playEffect(p.getLocation(), Effect.BOW_FIRE, 0);
            Vector vec = p.getEyeLocation().getDirection();

            for (int i = 0; i < 10; i++)
            {
                Arrow arrow = p.getWorld().spawnArrow(p.getLocation().add(0, 2, 0), vec, 2F, 25F);
                arrow.setShooter(p);
                arrow.setColor(Color.fromBGR((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
                arrow.setGravity(false);
                arrow.setCustomName("ShotGun");

                p.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 20);
            }
        }

        private static int count;

        public static void sniperGun(Player p)
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
                        Arrow arrow = p.getWorld().spawnArrow(p.getLocation().add(0, 2, 0), vec, 10F, 0F);
                        arrow.setShooter(p);
                        arrow.setColor(Color.BLACK);
                        arrow.setGravity(false);
                        arrow.setCustomName("SniperGun");

                        p.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 10 * 20);
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

        public static void explodingGun(Player player)
        {
            player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 0);
            Vector vec = player.getEyeLocation().getDirection();
            Arrow arrow = player.getWorld().spawnArrow(player.getLocation().add(0, 2, 0), vec, 4F, 1F);
            arrow.setShooter(player);
            arrow.setColor(Color.fromBGR((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            arrow.setGravity(false);
            arrow.setCustomName("ExplodingGun");

            player.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 10 * 20);
        }

        public static void flameThrowerGun(Player player) //火炎放射器
        {
            player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
            Vector vec = player.getEyeLocation().getDirection();

            SmallFireball fireball = player.getWorld().spawn(player.getEyeLocation().add(0, 0.5, 0), SmallFireball.class);
            fireball.setShooter(player);
            fireball.setGravity(true);
            fireball.setVelocity(vec);

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    SmallFireball fireball = player.getWorld().spawn(player.getEyeLocation().add(0, 0.5, 0), SmallFireball.class);
                    fireball.setShooter(player);
                    fireball.setGravity(false);
                    fireball.setVelocity(vec);

                    player.getServer().getScheduler().runTaskLater(plugin, fireball :: remove, 10);
                }
            }.runTaskLater(plugin, 3);

            player.getServer().getScheduler().runTaskLater(plugin, fireball :: remove, 10);
        }

        public static void searchGun(Player player)
        {
            List<Block> blockList = player.getLineOfSight(null, 100);
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

            Collection<Entity> targetList =
                    targetLocation.getWorld().getNearbyEntities(targetLocation, 20, 20, 20, Predicate);
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

        public static void potionGun(Player player) //ポーション散布銃
        {
            player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 0);
            Vector vec = player.getEyeLocation().getDirection();
            Arrow arrow = player.getWorld().spawnArrow(player.getLocation().add(0, 2, 0), vec, 1.5F, 4F);
            arrow.setShooter(player);
            arrow.setColor(Color.fromBGR((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            arrow.setGravity(false);
            arrow.setCustomName("PotionGun");

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
            }, 5, 5);

            player.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 10 * 20);
        }

        public static void handGun(Player player)
        {
            player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 0);
            Vector vec = player.getEyeLocation().getDirection();
            Arrow arrow = player.getWorld().spawnArrow(player.getLocation().add(0, 2, 0), vec, 2F, 2F);
            arrow.setShooter(player);
            arrow.setColor(Color.BLUE);
            arrow.setGravity(false);
            arrow.setCustomName("HandGun");

            player.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 10 * 20);
        }
    }
}
