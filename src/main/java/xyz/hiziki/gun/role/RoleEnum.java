package xyz.hiziki.gun.role;

import xyz.hiziki.gun.gun.GunEnum;

import java.util.ArrayList;
import java.util.List;

public enum RoleEnum
{
    ASSAULT_SOLDIER,
    SCOUT,
    REINFORCEMENTS,
    SNIPER,
    DESTROYER,
    SPECIAL_SOLDIER;

    public static List<GunEnum> getRoleGun(RoleEnum role)
    {
        List<GunEnum> list = new ArrayList<>();

        switch (role)
        {
            case ASSAULT_SOLDIER ->
            {
                list.add(GunEnum.SHOT_GUN);
                list.add(GunEnum.HAND_GUN);
            }
            case SCOUT ->
            {
                list.add(GunEnum.AUTOMATIC_GUN);
                list.add(GunEnum.SEARCH_GUN);
            }
            case REINFORCEMENTS ->
            {
                list.add(GunEnum.SEARCH_GUN);
                list.add(GunEnum.POTION_GUN);
            }
            case SNIPER ->
            {
                list.add(GunEnum.SNIPER_GUN);
                list.add(GunEnum.HAND_GUN);
            }
            case DESTROYER ->
            {
                list.add(GunEnum.ABSORPTION_GUN);
                list.add(GunEnum.SEARCH_GUN);
            }
            case SPECIAL_SOLDIER ->
            {
                list.add(GunEnum.FLAME_THROWER_GUN);
                list.add(GunEnum.POTION_GUN);
            }
        }
        return list;
    }
}