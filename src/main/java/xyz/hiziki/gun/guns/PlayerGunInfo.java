package xyz.hiziki.gun.guns;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.hiziki.gun.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerGunInfo
{
    public List<GunInfo> GunInfoList;

    public Player Player;

    private final JavaPlugin plugin = Main.getPlugin();

    public PlayerGunInfo(Player Player)
    {
        this.Player = Player;
        this.GunInfoList = new ArrayList<>();
        this.setGun();
    }

    public void setGun()
    {
        GunInfoList.clear();
        for (GunItemEnum kind : GunItemEnum.values())
        {
            GunInfoList.add(new GunInfo(kind));
        }
        GunItem.setGun(this.Player);
    }

    public String viewBullet(ItemStack item)
    {
        GunInfo guninfo = GunInfoList.stream().filter(v -> Objects.equals(v.GunKind.getGunItemStack(), item)).findFirst().orElse(null);
        if (guninfo == null)
        {
            return null;
        }

        if (guninfo.ReloadNowFlg)
        {
            return "リロード中" + guninfo.NowReloadTime + "s待ってね";
        }
        else
        {
            return !guninfo.ReloadFlg ? guninfo.NowBullet + "/" + guninfo.GunKind.getBullet() : "リロードが必要だ！しゃがんでリロードしよう";
        }
    }

    public void fire(GunItemEnum GunKind)
    {
        GunInfo guninfo = GunInfoList.stream().filter(v -> Objects.equals(v.GunKind, GunKind)).findFirst().orElse(null);
        guninfo.fire(this.Player);
        this.coolDown(guninfo);
    }

    public void reload(GunItemEnum GunKind)
    {
        GunInfo guninfo = GunInfoList.stream().filter(v -> Objects.equals(v.GunKind, GunKind)).findFirst().orElse(null);
        if (guninfo.ReloadNowFlg)
        {
            return;
        }

        guninfo.reloadStart();
        this.Player.playSound(Player.getLocation(), Sound.BLOCK_SMITHING_TABLE_USE, 1, 1);
        this.Player.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
        {
            try
            {
                for (int i = 0; i < guninfo.GunKind.getReloadTime(); i++)
                {
                    Thread.sleep(1000);
                    guninfo.NowReloadTime--;
                    viewBullet();
                }
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

            guninfo.reloadEnd();
            viewBullet();
        });
    }

    public void viewBullet()
    {
        String tmp = this.viewBullet(this.Player.getInventory().getItemInMainHand());
        if (tmp == null) return;
        TextComponent component = new TextComponent();
        component.setText(tmp);
        this.Player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
    }

    private void coolDown(GunInfo guninfo)
    {
        //クールダウン中は動かさない
        if (guninfo.CoolDownNowFlg) return;

        if (guninfo.GunKind.getCoolDownTime() == 0)
        {
            guninfo.CoolDownFlg = false;
            return;
        }

        guninfo.CoolDownNowFlg = true;

        this.Player.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () ->
        {
            guninfo.CoolDownFlg = false;
            guninfo.CoolDownNowFlg = false;
        }, (int) (guninfo.GunKind.getCoolDownTime() * 20));
    }

    private static class GunInfo
    {
        public GunItemEnum GunKind;

        public int NowBullet;

        public Boolean CoolDownFlg;

        public Boolean CoolDownNowFlg;

        public Boolean ReloadFlg;

        public Boolean ReloadNowFlg;

        public int NowReloadTime;

        public GunInfo(GunItemEnum GunKind)
        {
            this.GunKind = GunKind;
            this.NowBullet = GunKind.getBullet();
            this.CoolDownFlg = false;
            this.ReloadFlg = false;
            this.ReloadNowFlg = false;
            this.CoolDownNowFlg = false;
            this.NowReloadTime = GunKind.getReloadTime();
        }

        private void fire(Player player)
        {
            if (!ReloadFlg && !ReloadNowFlg && !CoolDownFlg) //銃を撃てるかどうか
            {
                switch (this.GunKind)
                {
                    case AUTOMATIC_GUN -> GunItem.Event.automaticGun(player);
                    case SHOT_GUN -> GunItem.Event.shotGun(player);
                    case SNIPER_GUN -> GunItem.Event.sniperGun(player);
                    case EXPLODING_GUN -> GunItem.Event.explodingGun(player);
                    case FLAME_THROWER_GUN -> GunItem.Event.flameThrowerGun(player);
                    case SEARCH_GUN -> GunItem.Event.searchGun(player);
                    case POTION_GUN -> GunItem.Event.potionGun(player);
                    case HAND_GUN -> GunItem.Event.handGun(player);
                }
                CoolDownFlg = true;
                NowBullet--;
                ReloadFlg = NowBullet <= 0;
            }
        }

        private void reloadStart()
        {
            this.ReloadNowFlg = true;
            this.NowReloadTime = GunKind.getReloadTime();
        }

        private void reloadEnd()
        {
            this.ReloadNowFlg = false;
            this.ReloadFlg = false;
            this.NowBullet = GunKind.getBullet();
        }
    }
}
