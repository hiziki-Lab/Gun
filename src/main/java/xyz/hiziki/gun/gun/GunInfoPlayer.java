package xyz.hiziki.gun.gun;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.role.RoleEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GunInfoPlayer
{
    public List<GunInfo> gunInfoList;

    public Player player;

    private final JavaPlugin plugin = Main.getPlugin();

    public GunInfoPlayer(Player _player)
    {
        player = _player;
        gunInfoList = new ArrayList<>();
        setGun();
    }

    public void setGun()
    {
        gunInfoList.clear();

        List<GunEnum> guns;
        Inventory inv = player.getInventory();

        RoleEnum playerRole = Main.getPlayerRole().get(player);

        guns = RoleEnum.getRoleGun(playerRole);

        int count = 0;

        for (GunEnum gun : guns)
        {
            count++;

            gunInfoList.add(new GunInfoPlayer.GunInfo(gun));
            inv.setItem(count, gun.getGunItemStack());
        }

        player.sendMessage(ChatColor.AQUA + "あなたのロールは" + playerRole + "です。");

    }

    public String bullet(ItemStack item)
    {
        GunInfo guninfo = gunInfoList.stream().filter(v -> Objects.equals(v.gunKind.getGunItemStack(), item)).findFirst().orElse(null);

        if (guninfo == null)
        {
            return null;
        }

        if (guninfo.reloadNowFlg)
        {
            return "リロード中" + guninfo.nowReloadTime + "s待ってね";
        }
        else
        {
            return !guninfo.reloadFlg ? guninfo.nowBullet + "/" + guninfo.gunKind.getBullet() : "リロードが必要だ！しゃがんでリロードしよう";
        }
    }

    public void fire(GunEnum gunKind)
    {
        GunInfo guninfo = gunInfoList.stream().filter(v -> Objects.equals(v.gunKind, gunKind)).findFirst().orElse(null);
        guninfo.fire(player);
        coolDown(guninfo);
    }

    public void reload(GunEnum gunKind)
    {
        GunInfo guninfo = gunInfoList.stream().filter(v -> Objects.equals(v.gunKind, gunKind)).findFirst().orElse(null);

        if (guninfo.reloadNowFlg)
        {
            return;
        }

        guninfo.reloadStart();

        player.playSound(player.getLocation(), Sound.BLOCK_SMITHING_TABLE_USE, 1, 1);

        player.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
        {
            try
            {
                for (int i = 0; i < guninfo.gunKind.getReloadTime(); i++)
                {
                    Thread.sleep(1000);
                    guninfo.nowReloadTime--;
                    viewBullet();
                }
            }
            catch (InterruptedException ex)
            {
                throw new RuntimeException(ex);
            }

            guninfo.reloadEnd();
            viewBullet();
        });
    }

    public void viewBullet()
    {
        String tmp = bullet(player.getInventory().getItemInMainHand());

        if (tmp != null)
        {
            TextComponent component = new TextComponent();
            component.setText(tmp);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
        }
    }

    private void coolDown(GunInfo gunInfo)
    {
        //クールダウン中は動かさない
        if (gunInfo.coolDownNowFlg) return;

        if (gunInfo.gunKind.getCoolDownTime() == 0)
        {
            gunInfo.coolDownFlg = false;
            return;
        }

        gunInfo.coolDownNowFlg = true;

        player.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () ->
        {
            gunInfo.coolDownFlg = false;
            gunInfo.coolDownNowFlg = false;
        }, (int) (gunInfo.gunKind.getCoolDownTime() * 20));
    }

    public static class GunInfo
    {
        public GunEnum gunKind;

        public int nowBullet;

        public Boolean coolDownFlg;

        public Boolean coolDownNowFlg;

        public Boolean reloadFlg;

        public Boolean reloadNowFlg;

        public int nowReloadTime;

        public GunInfo(GunEnum _gunKind)
        {
            gunKind = _gunKind;
            nowBullet = _gunKind.getBullet();
            coolDownFlg = false;
            reloadFlg = false;
            reloadNowFlg = false;
            coolDownNowFlg = false;
            nowReloadTime = gunKind.getReloadTime();
        }

        private void fire(Player player)
        {
            if (!reloadFlg && !reloadNowFlg && !coolDownFlg) //銃を撃てるかどうか
            {
                switch (gunKind)
                {
                    case AUTOMATIC_GUN -> new GunEvent().automaticGun(player);
                    case SHOT_GUN -> new GunEvent().shotGun(player);
                    case SNIPER_GUN -> new GunEvent().sniperGun(player);
                    case ABSORPTION_GUN -> new GunEvent().absorptionGun(player);
                    case FLAME_THROWER_GUN -> new GunEvent().flameThrowerGun(player);
                    case SEARCH_GUN -> new GunEvent().searchGun(player);
                    case POTION_GUN -> new GunEvent().potionGun(player);
                    case HAND_GUN -> new GunEvent().handGun(player);
                }
                coolDownFlg = true;
                nowBullet--;
                reloadFlg = nowBullet <= 0;
            }
        }

        private void reloadStart()
        {
            reloadNowFlg = true;
            nowReloadTime = gunKind.getReloadTime();
        }

        private void reloadEnd()
        {
            reloadNowFlg = false;
            reloadFlg = false;
            nowBullet = gunKind.getBullet();
        }
    }
}
