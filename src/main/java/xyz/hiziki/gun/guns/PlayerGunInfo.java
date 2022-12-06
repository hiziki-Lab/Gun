package xyz.hiziki.gun.guns;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Effect;
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
        this.SetGun();
    }

    public void SetGun()
    {
        GunInfoList.clear();
        for (GunItemEnum kind : GunItemEnum.values())
        {
            GunInfoList.add(new GunInfo(kind));
        }
        GunItem.SetGun(this.Player);
    }

    public String ViewBullet(ItemStack item)
    {
        GunInfo guninfo = GunInfoList.stream().filter(v -> Objects.equals(v.GunKind.getGunItemStack(), item)).findFirst().orElse(null);
        if (guninfo == null) return null;
        if (guninfo.ReloadNowFig) return "リロード中" + guninfo.NowReloadTime + "s待ってね";
        return !guninfo.ReloadFlg ? guninfo.GunKind.getBullet() + "/" + guninfo.NowBullet : "リロードが必要だ！しゃがんでリロードしよう";
    }

    public void Fire(GunItemEnum GunKind)
    {
        GunInfo guninfo = GunInfoList.stream().filter(v -> Objects.equals(v.GunKind, GunKind)).findFirst().orElse(null);
        guninfo.Fire(this.Player);
        this.CoolDown(guninfo);
    }

    public void Reload(GunItemEnum GunKind)
    {
        GunInfo guninfo = GunInfoList.stream().filter(v -> Objects.equals(v.GunKind, GunKind)).findFirst().orElse(null);
        if (guninfo.ReloadNowFig) return;
        guninfo.ReloadStart();
        this.Player.getWorld().playEffect(this.Player.getLocation(), Effect.END_GATEWAY_SPAWN, 0);
        this.Player.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
        {
            try
            {
                for (int i = 0; i < guninfo.GunKind.getReloadTime(); i++)
                {
                    Thread.sleep(1000);
                    guninfo.NowReloadTime--;
                    ViewBullet();
                }
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

            guninfo.ReloadEnd();
            ViewBullet();
        });
    }

    /**
     * 打てるか否か
     */
    public Boolean Can(GunItemEnum GunKind) //使われてないですね？
    {
        GunInfo guninfo = GunInfoList.stream().filter(v -> Objects.equals(v.GunKind, GunKind)).findFirst().orElse(null);
        return guninfo.Can();
    }

    public void ViewBullet()
    {
        String tmp = this.ViewBullet(this.Player.getInventory().getItemInMainHand());
        if (tmp == null) return;
        TextComponent component = new TextComponent();
        component.setText(tmp);
        this.Player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
    }

    private void CoolDown(GunInfo guninfo)
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

        public Boolean ReloadNowFig;

        public int NowReloadTime;

        public GunInfo(GunItemEnum GunKind)
        {
            this.GunKind = GunKind;
            this.NowBullet = GunKind.getBullet();
            this.CoolDownFlg = false;
            this.ReloadFlg = false;
            this.ReloadNowFig = false;
            this.CoolDownNowFlg = false;
            this.NowReloadTime = GunKind.getReloadTime();
        }

        /**
         * その銃を打つことができるか
         */
        private Boolean Can()
        {
            return !ReloadFlg && !ReloadNowFig && !CoolDownFlg;
        }

        private void Fire(Player player)
        {
            if (this.Can())
            {
                switch (this.GunKind)
                {
                    case NORMAL_GUN ->
                    {
                        GunItem.Event.ArrowGunRod(player);
                        CoolDownFlg = true;
                    }
                    case LIGHTNING_GUN ->
                    {
                        GunItem.Event.LightningGunRod(player);
                        CoolDownFlg = true;
                    }
                    case SHOT_GUN ->
                    {
                        GunItem.Event.SmallFireBallGunRod(player);
                        CoolDownFlg = true;
                    }
                    case FLAME_THROWER_GUN ->
                    {
                        GunItem.Event.FlameThrowerGun(player);
                        CoolDownFlg = true;
                    }
                    case SEARCH_GUN ->
                    {
                        GunItem.Event.SearchGunRod(player);
                        CoolDownFlg = true;
                    }
                    case TARGET_GUN ->
                    {
                        GunItem.Event.TargetGunRod(player);
                        CoolDownFlg = true;
                    }
                    case POTION_GUN ->
                    {
                        GunItem.Event.potionGunRos(player);
                        CoolDownFlg = true;
                    }
                }
                NowBullet--;
                ReloadFlg = NowBullet <= 0;
            }

        }

        private void ReloadStart()
        {
            this.ReloadNowFig = true;
            this.NowReloadTime = GunKind.getReloadTime();

        }

        private void ReloadEnd()
        {
            this.ReloadNowFig = false;
            this.ReloadFlg = false;
            this.NowBullet = GunKind.getBullet();

        }
    }
}
