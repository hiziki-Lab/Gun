package xyz.hiziki.gun.command;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.hiziki.gun.command.start.StartCommandCompleter;

/*
* コマンドを管理するクラス
* 今はTabCompleterだけだがCommandExecutorも追加予定
* */
public class CommandManager
{
    public CommandManager(JavaPlugin plugin)//コンストラクター
    {
        plugin.getCommand("GameStart").setTabCompleter(new StartCommandCompleter());
    }
}
