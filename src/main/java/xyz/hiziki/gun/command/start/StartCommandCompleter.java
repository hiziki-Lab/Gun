package xyz.hiziki.gun.command.start;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class StartCommandCompleter implements TabCompleter
{
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> list = new ArrayList<>(); //List作成

        switch (args.length)
        {
            case 1 ->
            {
                list.add("enableRole");
                list.add("disableRole");
            }
            case 2 ->
            {
                list.add("team");
                list.add("survival");
                list.add("solo");
            }
            case 3 ->
            {
                list.add("exception_deleteRole");
                list.add("exception_addRole");
            }
            default ->
            {
                list.add("Assault_Soldier");
                list.add("Scout");
                list.add("Reinforcements");
                list.add("Sniper");
                list.add("Destroyer");
                list.add("special_soldier");
            }
        }
        return list; //list
    }
}