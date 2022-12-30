package xyz.hiziki.gun.role;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RoleInfoPlayer
{
    public List<RoleInfo> roleInfoList;

    private Player player;

    public RoleInfoPlayer(Player _player)
    {
        player = _player;
        roleInfoList = new ArrayList<>();
        setRole();
    }

    private void setRole()
    {

    }


    public static class RoleInfo
    {

    }
}
