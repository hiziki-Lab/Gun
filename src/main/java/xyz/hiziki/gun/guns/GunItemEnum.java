package xyz.hiziki.gun.guns;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.Objects;

public enum GunItemEnum
{
    NORMAL_GUN(GunItem.Info.NormalGun(), 25, 5, 0),
    SHOT_GUN(GunItem.Info.ShotGun(), 6, 5, 0.5),
    LIGHTNING_GUN(GunItem.Info.LightningGun(), 10, 10, 5),
    FLAME_THROWER_GUN(GunItem.Info.FlameThrowerGun(), 20, 10, 0),
    SEARCH_GUN(GunItem.Info.SearchGun(), 10, 10, 10),
    TARGET_GUN(GunItem.Info.TargetGun(), 5, 10, 0),
    POTION_GUN(GunItem.Info.PotionGun(), 3, 10, 1);

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
        PlayerInventory ihv = Player.getInventory();
        return Arrays.stream(GunItemEnum.values()).filter(v ->
                Objects.equals(v.getGunItemStack(), ihv.getItemInMainHand())).findFirst().orElse(null);
    }
}

