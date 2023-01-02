package xyz.hiziki.gun.event.events;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.util.GunGameMode;

public class EntityDamageByEntity
{
    private final JavaPlugin plugin = Main.getPlugin();

    public EntityDamageByEntity(EntityDamageByEntityEvent e) //コンストラクタ
    {
        if (Main.getGameMode() != GunGameMode.NONE)
        {
            playerHitBullet(e);
        }
    }

    private void playerHitBullet(EntityDamageByEntityEvent e) //弾が当たった時
    {
        if (e.getEntity() instanceof Player p)
        {
            if (e.getDamager() instanceof Arrow arrow)
            {
                if (arrow.getCustomName() != null)
                {
                    switch (arrow.getCustomName())
                    {
                        case "automaticGun", "shotGun" -> e.setDamage(2);
                        case "sniperGun" -> e.setDamage(16);
                        case "absorptionGun" -> e.setDamage(3);
                        case "searchGun" ->
                        {
                            e.setDamage(0);

                            Firework firework = p.getWorld().spawn(p.getLocation(), Firework.class);
                            FireworkMeta meta = firework.getFireworkMeta();
                            meta.addEffects(FireworkEffect.builder().withColor(Color.GREEN).with(
                                    FireworkEffect.Type.BALL_LARGE).withFlicker().build());
                            meta.setPower(1);
                            firework.setFireworkMeta(meta);
                        }
                        case "potionGun" ->
                        {
                            e.setDamage(0);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 5));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 5));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 5));
                        }
                        case "handGun" -> e.setDamage(1.5);
                    }
                    plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> p.setNoDamageTicks(0), 2L);
                }
            }
        }
        else
        {
            e.setDamage(0);
        }
    }
}