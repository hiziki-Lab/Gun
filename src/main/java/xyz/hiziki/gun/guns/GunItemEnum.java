package xyz.hiziki.gun.guns;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.hiziki.gun.util.Util;

import java.util.Arrays;
import java.util.Objects;

public enum GunItemEnum
{
    AUTOMATIC_GUN(Util.itemMeta(Material.MUSIC_DISC_13, ChatColor.GOLD + "自動小銃"),
            25, 5, 0),

    SHOT_GUN(Util.itemMeta(Material.MUSIC_DISC_CAT, ChatColor.GOLD + "散弾銃"),
            6, 5, 0.5),

    SNIPER_GUN(Util.itemMeta(Material.MUSIC_DISC_BLOCKS, ChatColor.GOLD + "狙撃銃"),
            4, 6, 6),

    ABSORPTION_GUN(Util.itemMeta(Material.MUSIC_DISC_CHIRP, ChatColor.GOLD + "吸収銃"),
            20, 5, 0.25),

    FLAME_THROWER_GUN(Util.itemMeta(Material.MUSIC_DISC_FAR, ChatColor.GOLD + "火炎放射器"),
            20, 10, 0),

    SEARCH_GUN(Util.itemMeta(Material.MUSIC_DISC_MELLOHI, ChatColor.GOLD + "索敵銃"),
            10, 10, 10),

    POTION_GUN(Util.itemMeta(Material.MUSIC_DISC_STAL, ChatColor.GOLD + "ポーション散布銃"),
            10, 5, 1),

    HAND_GUN(Util.itemMeta(Material.MUSIC_DISC_STRAD, ChatColor.GOLD + "拳銃"),
            12, 4, 0.6);

    private final ItemStack gunItemStack; //アイテム情報
    private final int bullet; //球数
    private final int reloadTime; //リロード時間
    private final double coolDownTime; //発射後のクールダウン

    GunItemEnum(ItemStack itemStack, int bullet, int reloadTime, double coolDownTime)
    {
        this.gunItemStack = itemStack;
        this.bullet = bullet;
        this.reloadTime = reloadTime;
        this.coolDownTime = coolDownTime;
    }

    public ItemStack getGunItemStack() //getter
    {
        return gunItemStack;
    }

    public int getBullet() //getter
    {
        return bullet;
    }

    public int getReloadTime() //getter
    {
        return reloadTime;
    }

    public double getCoolDownTime() //getter
    {
        return coolDownTime;
    }

    public static GunItemEnum getKind(Player p) //getter
    {
        return Arrays.stream(GunItemEnum.values()).filter(v -> Objects.equals(v.getGunItemStack(),
                p.getInventory().getItemInMainHand())).findFirst().orElse(null);
    }
}

