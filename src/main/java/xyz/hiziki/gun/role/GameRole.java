package xyz.hiziki.gun.role;

import xyz.hiziki.gun.gun.GunItemEnum;

import java.util.ArrayList;
import java.util.List;

public enum GameRole
{
    ASSAULT_SOLDIER,
    SCOUT,
    REINFORCEMENTS,
    SNIPER,
    DESTROYER,
    SPECIAL_SOLDIER;

    public static List<GunItemEnum> getRoleGun(GameRole role)
    {
        List<GunItemEnum> list = new ArrayList<>();

        switch (role)
        {
            case ASSAULT_SOLDIER ->
            {
                list.add(GunItemEnum.SHOT_GUN);
                list.add(GunItemEnum.HAND_GUN);
            }
            case SCOUT ->
            {
                list.add(GunItemEnum.AUTOMATIC_GUN);
                list.add(GunItemEnum.SEARCH_GUN);
            }
            case REINFORCEMENTS ->
            {
                list.add(GunItemEnum.SEARCH_GUN);
                list.add(GunItemEnum.POTION_GUN);
            }
            case SNIPER ->
            {
                list.add(GunItemEnum.SNIPER_GUN);
                list.add(GunItemEnum.HAND_GUN);
            }
            case DESTROYER ->
            {
                list.add(GunItemEnum.ABSORPTION_GUN);
                list.add(GunItemEnum.SEARCH_GUN);
            }
            case SPECIAL_SOLDIER ->
            {
                list.add(GunItemEnum.FLAME_THROWER_GUN);
                list.add(GunItemEnum.POTION_GUN);
            }
        }
        return list;
    }
}