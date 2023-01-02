package xyz.hiziki.gun.command.start;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.gun.GunInfoPlayer;
import xyz.hiziki.gun.team.ScoreboardSetter;
import xyz.hiziki.gun.util.GunGameMode;
import xyz.hiziki.gun.util.RemoveGun;

import java.util.ArrayList;

public class StartCommandExecutor implements CommandExecutor
{
    private final JavaPlugin plugin = Main.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // GameStartコマンド が実行された時に実行

        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.RED + "サブコマンドが設定されていません。");
            return true;
        }
        else
        {
            switch (args[0])
            {
                case "solo" -> Main.setGameMode(GunGameMode.SOLO);
                case "oneLife" -> Main.setGameMode(GunGameMode.ONE_LIFE);
                case "team" ->
                {
                    Main.setScoreBoard(new ScoreboardSetter(Main.getPlugin().getServer()));
                    Main.setGameMode(GunGameMode.TEAM);
                }
                default ->
                {
                    sender.sendMessage("不明なサブコマンドです。");
                    return true;
                }
            }
        }

        Main.setGunInfoPlayerList(new ArrayList<>());

        for (Player target : plugin.getServer().getOnlinePlayers())
        {
            Main.getGunInfoPlayerList().add(new GunInfoPlayer(target));
        }

        //その他の設定
        if (Main.getBossBarTask() != null)
        {
            Main.getBossBarTask().cancel();
        }

        if (Main.getBossBar() != null)
        {
            Main.getBossBar().removeAll();
        }

        bossBarView();
        return true;
    }

    private void bossBarView()
    {
        //画面上記のBar設定
        Main.setBossBar(plugin.getServer().createBossBar("戦争時間", BarColor.BLUE, BarStyle.SEGMENTED_6,
                BarFlag.CREATE_FOG));

        Main.getBossBar().setProgress(1);

        for (GunInfoPlayer target : Main.getGunInfoPlayerList())
        {
            Main.getBossBar().addPlayer(target.player);
        }

        Main.setBossBarTask(Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
        {
            int Count = 0 ;

            @Override
            public void run()
            {
                double tmp = (1 - (Count / (((double) 10) * 60)));
                Count++;
                Main.getBossBar().setProgress(Math.abs(tmp));

                if (tmp < 0) //終了時
                {
                    for (GunInfoPlayer target : Main.getGunInfoPlayerList())
                    {
                        Main.setGameMode(GunGameMode.NONE);
                        target.player.sendTitle("終了です！！", "お疲れ様です。", 20, 200, 20);
                    }
                    new RemoveGun();
                    Main.getBossBar().removeAll();
                    Main.setGameMode(GunGameMode.NONE);
                }
                if (Main.getGameMode() == GunGameMode.NONE)
                {
                    Main.getBossBar().removeAll();
                    Main.getBossBarTask().cancel();
                }
            }
        },0,20));
    }
}
