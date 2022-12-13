package xyz.hiziki.gun.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.hiziki.gun.event.events.EntityDamageByEntity;

public class EventManager implements Listener
{
    public EventManager(JavaPlugin plugin) //コンストラクター
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        new EntityDamageByEntity(e);
    }
}
