package xyz.hiziki.gun.guns;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public enum GunItemEnum
{
    AUTOMATIC_GUN(GunItem.Info.automaticGun, 25, 5, 0),
    SHOT_GUN(GunItem.Info.shotGun, 6, 5, 0.5),
    SNIPER_GUN(GunItem.Info.sniperGun, 4, 6, 6),
    ABSORPTION_GUN(GunItem.Info.absorptionGun, 20, 5, 0.25),
    FLAME_THROWER_GUN(GunItem.Info.flameThrowerGun, 20, 10, 0),
    SEARCH_GUN(GunItem.Info.searchGun, 10, 10, 10),
    POTION_GUN(GunItem.Info.potionGun, 10, 5, 1),
    HAND_GUN(GunItem.Info.handGun, 12, 4, 0.6);

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

    public ItemStack getGunItemStack()
    {
        return gunItemStack;
    }

    public int getBullet()
    {
        return bullet;
    }

    public int getReloadTime()
    {
        return reloadTime;
    }

    public double getCoolDownTime()
    {
        return coolDownTime;
    }

    public static GunItemEnum getKind(Player Player)
    {
        return Arrays.stream(GunItemEnum.values()).filter(v -> Objects.equals(v.getGunItemStack(),
                Player.getInventory().getItemInMainHand())).findFirst().orElse(null);
    }
}

