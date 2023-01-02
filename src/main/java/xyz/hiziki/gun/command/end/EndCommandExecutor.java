package xyz.hiziki.gun.command.end;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.util.GunGameMode;
import xyz.hiziki.gun.util.RemoveGun;

public class EndCommandExecutor implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (Main.getBossBar() != null) //Null回避
        {
            Main.getBossBar().removeAll();
        }

        if (Main.getBossBarTask() != null) //Null回避
        {
            Main.getBossBarTask().cancel();
        }

        new RemoveGun();

        Main.setGameMode(GunGameMode.NONE);

        return true;
    }
}
