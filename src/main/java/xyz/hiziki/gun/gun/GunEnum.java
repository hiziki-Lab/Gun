package xyz.hiziki.gun.gun;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.hiziki.gun.util.Item;

import java.util.Arrays;
import java.util.Objects;

public enum GunEnum
{
    AUTOMATIC_GUN(Item.setMeta(Material.MUSIC_DISC_13, ChatColor.GOLD + "自動小銃"),
            25, 5, 0),

    SHOT_GUN(Item.setMeta(Material.MUSIC_DISC_CAT, ChatColor.GOLD + "散弾銃"),
            5, 5, 0.5),

    SNIPER_GUN(Item.setMeta(Material.MUSIC_DISC_BLOCKS, ChatColor.GOLD + "狙撃銃"),
            5, 5, 5),

    ABSORPTION_GUN(Item.setMeta(Material.MUSIC_DISC_CHIRP, ChatColor.GOLD + "吸収銃"),
            20, 5, 0.25),

    FLAME_THROWER_GUN(Item.setMeta(Material.MUSIC_DISC_FAR, ChatColor.GOLD + "火炎放射器"),
            20, 10, 0),

    SEARCH_GUN(Item.setMeta(Material.MUSIC_DISC_MELLOHI, ChatColor.GOLD + "索敵銃"),
            10, 10, 10),

    POTION_GUN(Item.setMeta(Material.MUSIC_DISC_STAL, ChatColor.GOLD + "ポーション散布銃"),
            10, 5, 1),

    HAND_GUN(Item.setMeta(Material.MUSIC_DISC_STRAD, ChatColor.GOLD + "拳銃"),
            12, 4, 0.6);

    private final ItemStack gunItemStack; //アイテム情報

    private final int bullet; //球数

    private final int reloadTime; //リロード時間

    private final double coolDownTime; //発射後のクールダウン

    GunEnum(ItemStack _itemStack, int _bullet, int _reloadTime, double _coolDownTime)
    {
        gunItemStack = _itemStack;
        bullet = _bullet;
        reloadTime = _reloadTime;
        coolDownTime = _coolDownTime;
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

    public static GunEnum getKind(Player p) //getter
    {
        return Arrays.stream(GunEnum.values()).filter(v -> Objects.equals(v.getGunItemStack(),
                p.getInventory().getItemInMainHand())).findFirst().orElse(null);
    }
}

