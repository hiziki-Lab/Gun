package xyz.hiziki.gun.guns;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.util.SearchBlock;

import java.util.ArrayList;
import java.util.Arrays;
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
        static ItemStack NormalGun()
        {
            ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "自動小銃");
            meta.setLore(new ArrayList<>(Arrays.asList("まゆマークが", "後ろに", "ついている")));
            item.setItemMeta(meta);
            return item;
        }

        static ItemStack ShotGun()
        {
            ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "散弾銃");
            meta.setLore(new ArrayList<>(Arrays.asList("真ん中の", "まゆさんマークが", "柔らかい")));
            item.setItemMeta(meta);
            return item;
        }

        static ItemStack FlameThrowerGun()
        {
            ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "火炎放射器");
            meta.setLore(new ArrayList<>(Arrays.asList("炎がでてくる", "でもよくみると", "赤いきのこ")));
            item.setItemMeta(meta);
            return item;
        }

        static ItemStack LightningGun()
        {
            ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "竜の息吹銃");
            meta.setLore(new ArrayList<>(Arrays.asList("棒の先端にドラゴンの頭", "でもよくみると", "まゆさん")));
            item.setItemMeta(meta);
            return item;
        }

        static ItemStack SearchGun()
        {
            ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "索敵銃");
            meta.setLore(new ArrayList<>(Arrays.asList("アンテナの", "代わりに", "まゆさんヘッド")));
            item.setItemMeta(meta);
            return item;
        }

        static ItemStack TargetGun()
        {
            ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "自動追尾銃");
            meta.setLore(new ArrayList<>(Arrays.asList("出ている玉、、、", "実は", "まゆさん説")));
            item.setItemMeta(meta);
            return item;
        }

        static ItemStack PotionGun()
        {
            ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "ポーション散布銃");
            meta.setLore(new ArrayList<>(Arrays.asList("一番", "弱い", "銃かもしれない")));
            item.setItemMeta(meta);
            return item;
        }
    }

    public static class Event
    {
        public static void ArrowGunRod(Player player)
        {
            JavaPlugin plugin = Main.getPlugin();

            player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 0);
            Vector vec = player.getEyeLocation().getDirection();
            Arrow arrow = player.getWorld().spawnArrow(player.getLocation().add(0, 2, 0), vec, 3F, 3F);
            arrow.setShooter(player);
            arrow.setColor(Color.fromBGR((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            arrow.setGravity(false);
            player.getServer().getScheduler().runTaskLater(plugin, arrow::remove, 10 * 20);
        }

        public static void SmallFireBallGunRod(Player player) //散弾銃
        {
            JavaPlugin plugin = Main.getPlugin();

            Vector vec = player.getEyeLocation().getDirection();

            for (int shotNo = 0; shotNo < 12; shotNo++)
            {
                Arrow arrow = player.getWorld().spawnArrow(player.getLocation().add(0, 2, 0), vec, 3F, 25F);
                arrow.setShooter(player);

                arrow.setColor(Color.fromBGR((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
                arrow.setGravity(false);
                player.getServer().getScheduler().runTaskLater(plugin, arrow :: remove, 20);
            }
        }

        public static void TargetGunRod(Player player) //追尾銃 廃止予定
        {
            JavaPlugin plugin = Main.getPlugin();

            player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);

            List<ShulkerBullet> shulkerBulletList = new ArrayList<>();
            for (int bulletNo = 0; bulletNo < 3; bulletNo++)
            {
                shulkerBulletList.add(player.getWorld().spawn(player.getLocation().add(0, 2, 0), ShulkerBullet.class));
            }

            for (ShulkerBullet bullet : shulkerBulletList)
            {
                bullet.setShooter(player);
                bullet.setVelocity(player.getVelocity().multiply(3));
            }

            //ターゲットするプレイヤーの絞り込み条件を設定
            Predicate<Entity> Predicate = entity ->
            {
                if (entity.getType() == EntityType.PLAYER)
                {
                    return !entity.getName().equals(player.getName());
                }
                return false;
            };
            RayTraceResult tmp = player.getWorld().rayTrace(player.getEyeLocation(),
                    player.getEyeLocation().getDirection(),
                    100, FluidCollisionMode.NEVER, false, 0.5, Predicate);

            if (tmp != null)
            {
                if (tmp.getHitEntity() != null)
                {
                    for (ShulkerBullet bullet : shulkerBulletList)
                    {
                        bullet.setTarget(tmp.getHitEntity());
                    }

                    tmp.getHitEntity().getWorld().spawnParticle(Particle.LAVA,
                            tmp.getHitEntity().getLocation().add(0, 1, 0), 10, 0, 0, 0, 1);
                }
            }

            player.getServer().getScheduler().runTaskTimer(plugin, () ->
            {
                for (ShulkerBullet bullet : shulkerBulletList)
                {
                    if (!bullet.isDead())
                    {
                        bullet.getWorld().spawnParticle(Particle.LAVA, bullet.getLocation().add(0, 1, 0),
                                10, 0, 0, 0, 1);
                    }
                }
            }, 10, 20);

            player.getServer().getScheduler().runTaskLater(plugin, () ->
            {
                for (ShulkerBullet bullet : shulkerBulletList)
                {
                    bullet.remove();
                }
            }, 20 * 20);
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
            Location targetLocation = SearchBlock.under(BlockLocation);

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
                Location tmp = entity.getLocation();

                for (int i = 0; i < 20; i++)
                {
                    entity.getWorld().spawnParticle(Particle.LAVA,
                            tmp.add(0, i, 0), 10, 0, 0, 0, 1);

                }
            }
        }

        public static void LightningGunRod(Player player) //廃止予定
        {
            JavaPlugin plugin = Main.getPlugin();

            List<Block> blockList = player.getLineOfSight(null, 10);
            Location BlockLocation = blockList.get(blockList.size() - 1).getLocation();
            Location targetLocation = SearchBlock.under(BlockLocation);
            player.getWorld().playEffect(player.getLocation(), Effect.ENDERDRAGON_GROWL, 0);
            //クリックした箇所を中心に一マス分設定
            List<Location> AroundLocation = new ArrayList<>();

            AroundLocation.add(new Location(targetLocation.getWorld(),
                    targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()).add(0, 1, 0));
            //前
            AroundLocation.add(new Location(targetLocation.getWorld(),
                    targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()).add(1, 1, 0));
            //右前
            AroundLocation.add(new Location(targetLocation.getWorld(),
                    targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()).add(1, 1, 1));
            //右
            AroundLocation.add(new Location(targetLocation.getWorld(),
                    targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()).add(0, 1, 1));
            //右後
            AroundLocation.add(new Location(targetLocation.getWorld(),
                    targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()).add(-1, 1, 1));
            //後
            AroundLocation.add(new Location(targetLocation.getWorld(),
                    targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()).add(-1, 1, 0));
            //左後
            AroundLocation.add(new Location(targetLocation.getWorld(),
                    targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()).add(-1, 1, -1));
            //左
            AroundLocation.add(new Location(targetLocation.getWorld(),
                    targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()).add(0, 1, -1));
            //左前
            AroundLocation.add(new Location(targetLocation.getWorld(),
                    targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()).add(1, 1, -1));

            for (Location loc : AroundLocation)
            {
                loc.getWorld().playEffect(loc, Effect.SMOKE, 10, 10);
                loc.getWorld().playEffect(loc, Effect.INSTANT_POTION_BREAK, 10, 10);
            }

            player.getServer().getScheduler().runTaskLater(plugin, new Runnable()
            {
                int taskID = 0;

                @Override
                public void run()
                {
                    player.getWorld().spawnEntity(targetLocation, EntityType.LIGHTNING);

                    taskID = player.getServer().getScheduler().runTaskTimer(plugin, new Runnable()
                    {
                        int count = 0;

                        @Override
                        public void run()
                        {
                            player.getWorld().spawnEntity(AroundLocation.get(count), EntityType.LIGHTNING);
                            count++;

                            if (count > AroundLocation.size() - 1)
                            {
                                player.getServer().getScheduler().cancelTask(taskID);
                            }
                        }
                    }, AroundLocation.size(), 10).getTaskId();

                }
            }, 30);
        }

        public static void potionGunRos(Player player) //ポーション散布銃
        {
            JavaPlugin plugin = Main.getPlugin();

            player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 0);
            Vector vec = player.getEyeLocation().getDirection();
            Arrow arrow = player.getWorld().spawnArrow(player.getLocation().add(0, 2, 0), vec, 0.1F, 8F);
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
            player.getServer().getScheduler().runTaskLater(plugin, arrow::remove, 10 * 20);
        }
    }
}
