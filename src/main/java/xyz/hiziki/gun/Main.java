package xyz.hiziki.gun;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitTask;
import xyz.hiziki.gun.command.CommandManager;
import xyz.hiziki.gun.guns.GunItemEnum;
import xyz.hiziki.gun.guns.PlayerGunInfo;
import xyz.hiziki.gun.util.GameGameMode;
import xyz.hiziki.gun.util.ScoreboardSetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Main extends JavaPlugin implements Listener
{
    private static JavaPlugin plugin;

    private GameGameMode gameMode;

    public BukkitTask BossBarTask;

    public BossBar bar;

    public ScoreboardSetter scoreBoard;

    public List<PlayerGunInfo> PlayerGunInfoList;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("GameStart"))
        {
            // TaskStartコマンド が実行された時に実行

            if(args.length == 0)
            {
                sender.sendMessage(ChatColor.RED + "サブコマンドが設定されていません。");
                return true;
            }
            else
            {
                switch (args[0])
                {
                    case "solo" -> gameMode = GameGameMode.SOLO;
                    case "survival" -> gameMode = GameGameMode.SURVIVAL;
                    case "team" ->
                    {
                        scoreBoard = new ScoreboardSetter(getServer());
                        gameMode = GameGameMode.TEAM;
                    }
                    default ->
                    {
                        sender.sendMessage("不明なサブコマンドです。");
                        return true;
                    }
                }
            }

            PlayerGunInfoList = new ArrayList<>();

            if (BossBarTask!= null)
            {
                BossBarTask.cancel();
            }

            for (Player target : getServer().getOnlinePlayers())
            {
                PlayerGunInfoList.add(new PlayerGunInfo(target));
            }


            if (bar != null)
            {
                bar.removeAll();
            }

            BossBarView();
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("GameEnd"))
        {
            // TaskStartコマンド が実行された時に実行
            bar.removeAll();

            if (BossBarTask!= null)
            {
                BossBarTask.cancel();
            }

            gameMode = GameGameMode.NONE;
            return true;
        }
        return false;
    }

    @Override
    public void onEnable()
    {
        // Plugin startup

        super.onEnable();

        plugin = this;

        getServer().getPluginManager().registerEvents(this, this);
        gameMode = GameGameMode.NONE;

        new CommandManager(plugin);

        getLogger().info("プラグインは正常に起動しました。");
    }

    @Override
    public void onDisable()
    {
        super.onDisable();

        getLogger().info("プラグインは正常に停止しました。");
    }

    @EventHandler
    public void PlayerItemHeldEvent(PlayerItemHeldEvent e)
    {
        if (gameMode != GameGameMode.NONE)
        {
            return;
        }

        getServer().getScheduler().runTaskLater(this, () ->
        {
            PlayerGunInfo target = GetPlayerGunInfo(e.getPlayer());
            //玉の残段数を表示
            target.ViewBullet();
        }, 1);

    }

    /**
     * リスポーンイベント
     */
    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent e)
    {
        if (gameMode == GameGameMode.NONE) return;
        PlayerGunInfo target = GetPlayerGunInfo(e.getPlayer());
        target.SetGun();
    }

    @EventHandler
    public void PlayerToggleSneakEvent(PlayerToggleSneakEvent e)
    {
        if (gameMode ==  GameGameMode.NONE) return;
        PlayerGunInfo target = GetPlayerGunInfo(e.getPlayer());
        GunItemEnum kind = GunItemEnum.GetKind(e.getPlayer());

        if (kind == null) return;

        //リロード処理
        target.Reload(kind);
        //玉の残段数を表示
        target.ViewBullet();
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e)
    {
        if (gameMode ==  GameGameMode.NONE) return;

        PlayerGunInfo target = GetPlayerGunInfo(e.getPlayer());
        GunItemEnum kind = GunItemEnum.GetKind(e.getPlayer());
        if (kind == null)
        {
            return;
        }

        target.Fire(kind);

        //玉の残段数を表示
        target.ViewBullet();
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent event)
    {
        if (gameMode ==  GameGameMode.NONE)
        {
            return;
        }

        if (event.getEntity().getKiller() == null)
        {
            return;
        }

        if (event.getEntity().getKiller().getType() != EntityType.PLAYER)
        {
            return;
        }

        switch (gameMode)
        {
            case TEAM :
                scoreBoard.PointCheck(event.getEntity().getKiller().getPlayer(), event.getEntity());
                break;
            case SOLO:
                break;
            case SURVIVAL:
                event.getEntity().setGameMode(GameMode.SPECTATOR);
                break;

        }

    }

    /**
     * プレイヤーリスト検索
     */
    private PlayerGunInfo GetPlayerGunInfo(Player Player)
    {
        return PlayerGunInfoList.stream().filter(v ->
                Objects.equals(v.Player.getUniqueId(), Player.getUniqueId())).findFirst().orElse(null);
    }

    private void BossBarView()
    {
        //画面上記のBar設定
        bar = getServer().createBossBar("戦争時間", BarColor.BLUE, BarStyle.SEGMENTED_6, BarFlag.CREATE_FOG);
        bar.setProgress(1);
        for (PlayerGunInfo target : PlayerGunInfoList)
        {
            bar.addPlayer(target.Player);
        }

        BossBarTask = Bukkit.getScheduler().runTaskTimer(this, new Runnable()
        {
            int Count = 0 ;

            @Override
            public void run()
            {
                double tmp = (1 - (Count / (((double) 10) * 60)));
                Count++;
                bar.setProgress(Math.abs(tmp));
                //終了時イベントをここに書く

                if(tmp < 0)
                {
                    for (PlayerGunInfo target : PlayerGunInfoList)
                    {
                        gameMode = GameGameMode.NONE;
                        target.Player.sendTitle("終了です！！", "お疲れ様です。", 20, 200, 20);
                    }
                    bar.removeAll();
                    gameMode = GameGameMode.NONE;
                }
                if(gameMode == GameGameMode.NONE)
                {
                    bar.removeAll();
                    BossBarTask.cancel();
                }

            }
        },0,20);
    }

    public static JavaPlugin getPlugin()
    {
        return plugin;
    }
}