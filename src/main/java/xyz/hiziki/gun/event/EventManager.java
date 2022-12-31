package xyz.hiziki.gun.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.hiziki.gun.event.events.EntityDamageByEntity;
import xyz.hiziki.gun.event.events.PlayerDeath;
import xyz.hiziki.gun.event.events.PlayerToggleSneak;

public class EventManager implements Listener
{
    public EventManager(JavaPlugin plugin) //コンストラクタ
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        new EntityDamageByEntity(e);
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e)
    {
        new PlayerDeath(e);
    }

    @EventHandler
    private void onPlayerToggleSneak(PlayerToggleSneakEvent e)
    {
        new PlayerToggleSneak(e);
    }
}
