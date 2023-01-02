package xyz.hiziki.gun;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import xyz.hiziki.gun.command.CommandManager;
import xyz.hiziki.gun.event.EventManager;
import xyz.hiziki.gun.gun.GunInfoPlayer;
import xyz.hiziki.gun.util.GunGameMode;
import xyz.hiziki.gun.team.ScoreboardSetter;

import java.util.*;

public final class Main extends JavaPlugin implements Listener
{
    private static JavaPlugin plugin;
    private static GunGameMode gameMode;
    private static BukkitTask bossBarTask;
    private static BossBar bossBar;
    private static ScoreboardSetter scoreBoard;
    private static List<GunInfoPlayer> gunInfoPlayerList;

    @Override
    public void onEnable()
    {
        //プラグイン起動時の処理
        super.onEnable();

        plugin = this;

        getServer().getPluginManager().registerEvents(this, this);

        gameMode = GunGameMode.NONE;

        new CommandManager(plugin);
        new EventManager(plugin);

        getLogger().info("プラグインは正常に起動しました。");
    }

    @Override
    public void onDisable()
    {
        //プラグイン停止時の処理
        super.onDisable();

        getLogger().info("プラグインは正常に停止しました。");
    }

    /*========== Getter ==========*/
    public static JavaPlugin getPlugin() //pluginのGetter
    {
        return plugin;
    }
    public static GunGameMode getGameMode() //GameModeのGetter
    {
        return gameMode;
    }
    public static BukkitTask getBossBarTask() //bossBarTaskのGetter
    {
        return bossBarTask;
    }
    public static BossBar getBossBar() //bossBarのGetter
    {
        return bossBar;
    }
    public static ScoreboardSetter getScoreBoard() //scoreBoardのGetter
    {
        return scoreBoard;
    }
    public static List<GunInfoPlayer> getGunInfoPlayerList() //gunInfoPlayerListのGetter
    {
        return gunInfoPlayerList;
    }
    public static GunInfoPlayer getPlayerGunInfo(Player player) //プレイヤーリストのGetter
    {
        return gunInfoPlayerList.stream().filter(v ->
                Objects.equals(v.player.getUniqueId(), player.getUniqueId())).findFirst().orElse(null);
    }

    /*========== Setter ==========*/
    public static void setGameMode(GunGameMode _gameMode) //GameModeのSetter
    {
        gameMode = _gameMode;
    }
    public static void setBossBarTask(BukkitTask _bossBarTask) //bossBarTaskのSetter
    {
        bossBarTask = _bossBarTask;
    }
    public static void setBossBar(BossBar _bossbar) //bossBarのSetter
    {
        bossBar = _bossbar;
    }
    public static void setScoreBoard(ScoreboardSetter _scoreBoard) //scoreBoardのSetter
    {
        scoreBoard = _scoreBoard;
    }
    public static void setGunInfoPlayerList(List<GunInfoPlayer> _gunInfoPlayerList) //gunInfoPlayerListのSetter
    {
        gunInfoPlayerList = _gunInfoPlayerList;
    }
}