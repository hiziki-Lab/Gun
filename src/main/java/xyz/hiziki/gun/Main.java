package xyz.hiziki.gun;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.hiziki.gun.command.CommandManager;
import xyz.hiziki.gun.event.EventManager;
import xyz.hiziki.gun.gun.GunEnum;
import xyz.hiziki.gun.gun.GunInfoPlayer;
import xyz.hiziki.gun.role.RoleInfoPlayer;
import xyz.hiziki.gun.util.GameGameMode;
import xyz.hiziki.gun.role.RoleEnum;
import xyz.hiziki.gun.team.ScoreboardSetter;

import java.util.*;

public final class Main extends JavaPlugin implements Listener
{
    private static JavaPlugin plugin;

    private static GameGameMode gameMode;

    public BukkitTask bossBarTask;

    public BossBar bar;

    public static ScoreboardSetter scoreBoard;

    public static List<GunInfoPlayer> gunInfoPlayerList;

    public List<RoleInfoPlayer> roleInfoPlayerList;

    private static HashMap<Player, RoleEnum> playerRole;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("GameStart"))
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
                    case "solo" -> gameMode = GameGameMode.SOLO;
                    case "oneLife" -> gameMode = GameGameMode.ONE_LIFE;
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

            gunInfoPlayerList = new ArrayList<>();

            for (Player target : getServer().getOnlinePlayers())
            {
                roleInfoPlayerList.add(new RoleInfoPlayer(target));
                gunInfoPlayerList.add(new GunInfoPlayer(target));
            }

            //その他の設定
            if (bossBarTask != null)
            {
                bossBarTask.cancel();
            }

            if (bar != null)
            {
                bar.removeAll();
            }

            bossBarView();
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("GameEnd"))
        {
            // GameEndコマンド が実行された時に実行

            if (bar != null) //Null回避
            {
                bar.removeAll();
            }

            if (bossBarTask != null) //Null回避
            {
                bossBarTask.cancel();
            }

            removeGun();

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
        playerRole = new HashMap<>();

        new CommandManager(plugin);
        new EventManager(plugin);

        getLogger().info("プラグインは正常に起動しました。");
    }

    @Override
    public void onDisable()
    {
        super.onDisable();

        getLogger().info("プラグインは正常に停止しました。");
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent e)
    {
        if (gameMode != GameGameMode.NONE)
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    GunInfoPlayer target = getPlayerGunInfo(e.getPlayer());
                    target.viewBullet(); //玉の残段数を表示
                }
            }.runTaskLater(plugin, 1);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (gameMode != GameGameMode.NONE)
        {
            GunInfoPlayer target = getPlayerGunInfo(e.getPlayer());
            GunEnum kind = GunEnum.getKind(e.getPlayer());

            if (kind != null)
            {
                target.fire(kind);//射撃
                target.viewBullet();//玉の残段数を表示
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {

    }

    public static GunInfoPlayer getPlayerGunInfo(Player player) //プレイヤーリスト検索
    {
        return gunInfoPlayerList.stream().filter(v ->
                Objects.equals(v.player.getUniqueId(), player.getUniqueId())).findFirst().orElse(null);
    }

    private void bossBarView()
    {
        //画面上記のBar設定
        bar = getServer().createBossBar("戦争時間", BarColor.BLUE, BarStyle.SEGMENTED_6, BarFlag.CREATE_FOG);
        bar.setProgress(1);

        for (GunInfoPlayer target : gunInfoPlayerList)
        {
            bar.addPlayer(target.player);
        }

        bossBarTask = Bukkit.getScheduler().runTaskTimer(this, new Runnable()
        {
            int Count = 0 ;

            @Override
            public void run()
            {
                double tmp = (1 - (Count / (((double) 10) * 60)));
                Count++;
                bar.setProgress(Math.abs(tmp));

                if (tmp < 0) //終了時
                {
                    for (GunInfoPlayer target : gunInfoPlayerList)
                    {
                        gameMode = GameGameMode.NONE;
                        target.player.sendTitle("終了です！！", "お疲れ様です。", 20, 200, 20);
                        removeGun();
                    }
                    bar.removeAll();
                    gameMode = GameGameMode.NONE;
                }
                if (gameMode == GameGameMode.NONE)
                {
                    bar.removeAll();
                    bossBarTask.cancel();
                }
            }
        },0,20);
    }

    private void removeGun()
    {
        for (Player target : plugin.getServer().getOnlinePlayers())
        {
            for (ItemStack item : target.getInventory())
            {
                if (item != null)
                {
                    switch (item.getType())
                    {
                        case MUSIC_DISC_13, MUSIC_DISC_CAT, MUSIC_DISC_BLOCKS, MUSIC_DISC_CHIRP,
                                MUSIC_DISC_FAR, MUSIC_DISC_MELLOHI, MUSIC_DISC_STAL, MUSIC_DISC_STRAD
                                -> target.getInventory().removeItem(item);
                    }
                }
            }
        }
    }

    public static JavaPlugin getPlugin()
    {
        return plugin;
    }

    public static GameGameMode getGameMode()
    {
        return gameMode;
    }

    public static ScoreboardSetter getScoreBoard()
    {
        return scoreBoard;
    }

    public static HashMap<Player, RoleEnum> getPlayerRole()
    {
        return playerRole;
    }
}