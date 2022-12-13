package xyz.hiziki.gun.guns;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public enum GunItemEnum
{
    NORMAL_GUN(GunItem.Info.AutomaticGun(), 25, 5, 0),
    SHOT_GUN(GunItem.Info.ShotGun(), 6, 5, 0.5),
    SNIPER_GUN(GunItem.Info.SniperGun(), 4, 6, 5),
    EXPLODING_GUN(GunItem.Info.ExplodingGun(), 1000, 0, 3),
    FLAME_THROWER_GUN(GunItem.Info.FlameThrowerGun(), 20, 10, 0),
    SEARCH_GUN(GunItem.Info.SearchGun(), 10, 10, 10),
    POTION_GUN(GunItem.Info.PotionGun(), 3, 10, 1),
    HAND_GUN(GunItem.Info.HandGun(), 12, 4, 0.6);

    private final ItemStack gunItemStack; //アイテム情報

    private final int Bullet; //球数

    private final int ReloadTime;

    private final double CoolDownTime;

    GunItemEnum(ItemStack _ItemStack, int _Bullet, int _ReloadTime, double _CoolDownTime)
    {
        this.gunItemStack = _ItemStack;
        this.Bullet = _Bullet;
        this.ReloadTime = _ReloadTime;
        this.CoolDownTime = _CoolDownTime;
    }

    public ItemStack getGunItemStack()
    {
        return gunItemStack;
    }

    public int getBullet()
    {
        return Bullet;
    }

    public int getReloadTime()
    {
        return ReloadTime;
    }

    public double getCoolDownTime()
    {
        return CoolDownTime;
    }

    public static GunItemEnum GetKind(Player Player)
    {
        return Arrays.stream(GunItemEnum.values()).filter(v -> Objects.equals(v.getGunItemStack(),
                Player.getInventory().getItemInMainHand())).findFirst().orElse(null);
    }
}

