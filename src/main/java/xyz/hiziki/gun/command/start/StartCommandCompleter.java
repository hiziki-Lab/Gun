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

        list.add("team");
        list.add("survival");
        list.add("solo");

        return list; //list
    }
}
