package net.skeagle.vrncore.utils.storage.timerewards;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;
import java.util.Random;

import static net.skeagle.vrncore.utils.VRNUtil.sayActionBar;
import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

@Getter
public class TimeRewards extends YamlConfig {
    private String permission;
    private Boolean use_permission;
    private List<String> commands;
    private List<String> messages;
    private Boolean use_actionbar;
    private String actionmessage;
    private Boolean fireworks;
    private Boolean enable_title;
    private String title;
    private String subtitle;

    TimeRewards(final String rewardTime) {
        setHeader("This is the time rewards file.",
                "Here you can set what events happen on each reward to make each one feel like a true accomplishment.\n\n");
        loadConfiguration("reward-template.yml", "rewards/" + rewardTime + (!rewardTime.endsWith(".yml") ? ".yml" : ""));
    }

    @Override
    protected void onLoadFinish() {
        permission = getString("permission");
        use_permission = getBoolean("use_permission");
        commands = getStringList("commands");
        messages = getStringList("messages");
        use_actionbar = getBoolean("use_actionbar");
        actionmessage = getString("actionmessage");
        fireworks = getBoolean("fireworks");
        enable_title = getBoolean("enable_title");
        title = getString("title");
        subtitle = getString("subtitle");
    }

    public void doReward(final Player p) {
        for (final String msg : messages) {
            sayNoPrefix(p, msg);
        }
        if (use_actionbar) {
            sayActionBar(p, actionmessage);
        }
        if (enable_title) {
            Remain.sendTitle(p, 0, 6 * 20, 2 * 20, title, subtitle);
        }
        for (String command : commands) {
            command = command.replaceAll("%player%", p.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        if (fireworks) {
            p.setInvulnerable(true);
            sendFirework(p.getLocation().add(0, 1, 0), buildFireworkEffect());
            Common.runLater(1, () -> p.setInvulnerable(false));
        }
    }

    public boolean checkPerm(final Player p) {
        return !use_permission || p.hasPermission(permission);
    }

    @Override
    public String getName() {
        return getFileName().replace(".yml", "");
    }

    private FireworkEffect buildFireworkEffect() {
        final FireworkEffect.Builder fwB = FireworkEffect.builder();
        final Random r = new Random();
        fwB.flicker(r.nextBoolean());
        fwB.trail(r.nextBoolean());
        fwB.with(FireworkEffect.Type.BALL);
        fwB.withColor(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        fwB.withFade(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        return fwB.build();
    }

    private void sendFirework(final Location loc, final FireworkEffect effect) {
        final CraftFirework fw = (CraftFirework) loc.getWorld().spawn(loc, Firework.class);
        final FireworkMeta fwmeta = fw.getFireworkMeta();
        fwmeta.setPower(5);
        fwmeta.addEffect(effect);
        fw.setFireworkMeta(fwmeta);
        fw.setTicksLived(2);
        fw.detonate();
    }
}
