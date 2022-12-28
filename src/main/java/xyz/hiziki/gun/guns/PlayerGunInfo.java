package xyz.hiziki.gun.guns;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.hiziki.gun.Main;
import xyz.hiziki.gun.util.GameRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerGunInfo
{
    public List<GunInfo> gunInfoList;

    public Player player;

    private final JavaPlugin plugin = Main.getPlugin();

    public PlayerGunInfo(Player p)
    {
        player = p;
        gunInfoList = new ArrayList<>();
        setGun(p);
    }

    public void setGun(Player p)
    {
        gunInfoList.clear();

        List<GunItemEnum> guns;
        Inventory inv = p.getInventory();

        if (Main.enableRole()) //ロールがonになっていたら
        {
            GameRole playerRole = Main.getPlayerRole().get(p);

            guns = GameRole.getRoleGun(playerRole);
            int count = 0;

            for (GunItemEnum gun : guns)
            {
                count++;

                gunInfoList.add(new GunInfo(gun));
                inv.setItem(count, gun.getGunItemStack());
            }

            p.sendMessage(ChatColor.AQUA + "あなたのロールは" + playerRole + "です。");
        }
        else //ロールがoffになっていたら
        {
            for (GunItemEnum gun : GunItemEnum.values())
            {
                gunInfoList.add(new GunInfo(gun));
                inv.setItem(gun.ordinal(), gun.getGunItemStack());
            }
        }
    }
    public String viewBullet(ItemStack item)
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

    public void fire(GunItemEnum gunKind)
    {
        GunInfo guninfo = gunInfoList.stream().filter(v -> Objects.equals(v.gunKind, gunKind)).findFirst().orElse(null);
        guninfo.fire(player);
        this.coolDown(guninfo);
    }

    public void reload(GunItemEnum gunKind)
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
        String tmp = this.viewBullet(player.getInventory().getItemInMainHand());
        if (tmp == null) return;
        TextComponent component = new TextComponent();
        component.setText(tmp);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
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

    private static class GunInfo
    {
        public GunItemEnum gunKind;

        public int nowBullet;

        public Boolean coolDownFlg;

        public Boolean coolDownNowFlg;

        public Boolean reloadFlg;

        public Boolean reloadNowFlg;

        public int nowReloadTime;

        public GunInfo(GunItemEnum gunKind)
        {
            this.gunKind = gunKind;
            this.nowBullet = gunKind.getBullet();
            this.coolDownFlg = false;
            this.reloadFlg = false;
            this.reloadNowFlg = false;
            this.coolDownNowFlg = false;
            this.nowReloadTime = gunKind.getReloadTime();
        }

        private void fire(Player player)
        {
            if (!reloadFlg && !reloadNowFlg && !coolDownFlg) //銃を撃てるかどうか
            {
                switch (this.gunKind)
                {
                    case AUTOMATIC_GUN -> GunItem.Event.automaticGun(player);
                    case SHOT_GUN -> GunItem.Event.shotGun(player);
                    case SNIPER_GUN -> GunItem.Event.sniperGun(player);
                    case ABSORPTION_GUN -> GunItem.Event.absorptionGun(player);
                    case FLAME_THROWER_GUN -> GunItem.Event.flameThrowerGun(player);
                    case SEARCH_GUN -> GunItem.Event.searchGun(player);
                    case POTION_GUN -> GunItem.Event.potionGun(player);
                    case HAND_GUN -> GunItem.Event.handGun(player);
                }
                coolDownFlg = true;
                nowBullet--;
                reloadFlg = nowBullet <= 0;
            }
        }

        private void reloadStart()
        {
            this.reloadNowFlg = true;
            this.nowReloadTime = gunKind.getReloadTime();
        }

        private void reloadEnd()
        {
            this.reloadNowFlg = false;
            this.reloadFlg = false;
            this.nowBullet = gunKind.getBullet();
        }
    }
}
