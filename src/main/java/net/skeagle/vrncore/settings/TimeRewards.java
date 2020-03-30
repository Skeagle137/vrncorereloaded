package net.skeagle.vrncore.settings;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;
import java.util.Random;

@Getter
public class TimeRewards extends YamlConfig {
    private List<String> RUN_COMMANDS;
    private List<String> MESSAGES;
    private Boolean ENABLE_FIREWORK;
    private Boolean ENABLE_TITLE;
    private String TITLE;
    private String SUBTITLE;

    public TimeRewards(final String rewardName) {
        setHeader(
                "This is the time rewards file.",
                "Here you can set what events happen on each reward to make each one feel like a true accomplishment."
        );
        loadConfiguration("rewards.yml", "rewards/" + rewardName + (!rewardName.endsWith(".yml") ? ".yml" : ""));
    }

    @Override
    protected void onLoadFinish() {
        RUN_COMMANDS = getStringList("Run_Commands");
        MESSAGES = getStringList("Messages");
        ENABLE_FIREWORK = getBoolean("Enable_Firework");
        ENABLE_TITLE = getBoolean("Enable_Firework");
        TITLE = getString("Title");
        SUBTITLE = getString("Subtitle");
    }

    public void doReward(final Player p) {
        for (final String commands : RUN_COMMANDS) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replaceAll("%player%", p.getName()));
            if (ENABLE_FIREWORK) {
                sendFirework(p.getLocation(), buildFireworkEffect());
            }
        }
    }

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
        fwmeta.setPower(2);
        fwmeta.addEffect(effect);
        fw.setFireworkMeta(fwmeta);
        fw.setTicksLived(2);
        fw.detonate();
    }
}
