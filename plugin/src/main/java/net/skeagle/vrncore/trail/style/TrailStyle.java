package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.hook.SuperVanishHook;
import net.skeagle.vrncore.playerdata.TrailData;
import net.skeagle.vrncore.trail.Particles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

import static net.skeagle.vrncore.trail.Particles.*;

public abstract class TrailStyle {

    protected TrailData data;
    protected Particles particle;
    protected int step;

    public TrailStyle(TrailData data) {
        this.updateData(data);
    }

    protected abstract void onTick(Entity target, Location loc);

    public boolean canApply(Particles particle) {
        return true;
    }

    public final void updateData(TrailData data) {
        this.data = data;
        this.particle = data.getParticle();
    }

    public final void step() {
        step++;
    }

    public final void tick(Entity target, Location loc) {
        onTick(target, loc);
    }

    public final void run(Entity target, Location loc, int amount, double speed, double off) {
        run(target, loc, amount, speed, off, off, off);
    }

    public final void run(Entity target, Location loc, int amount, double speed, double offX, double offY, double offZ) {
        Object particleOptions = null;
        if (particle.getProperties().length != 0) {
            List<ParticleProperties> props = Arrays.asList(particle.getProperties());
            if (props.contains(Particles.ParticleProperties.COLOR)) {
                switch (particle) {
                    case REDSTONE -> particleOptions = new Particle.DustOptions(data.getColor(), (float) data.getSize());
                    case NOTE -> {
                        amount = 0;
                        speed = 1;
                        offX = data.getNote() / 24D;
                        offY = 0;
                        offZ = 0;
                    }
                    case POTION -> {
                        speed = 1;
                        offX = data.getColor().getRed() / 255D;
                        offY = data.getColor().getGreen() / 255D;
                        offZ = data.getColor().getBlue() / 255D;
                    }
                }
            } else if (props.contains(ParticleProperties.COLOR_TRANSITION)) {
                particleOptions = new Particle.DustTransition(data.getColor(), data.getFade(), (float) data.getSize());
            }
        }
        if (particle == POTION) {
            for (int i = 0; i < amount; i++) {
                run(target, loc, 0, speed, offX, offY, offZ, particleOptions);
            }
            return;
        }
        run(target, loc, amount, speed, offX, offY, offZ, particleOptions);
    }

    private void run(Entity target, Location location, int amount, double speed, double offX, double offY, double offZ, Object options) {
        if (target instanceof Player player && HookManager.isSuperVanishLoaded() && SuperVanishHook.isVanished(player)) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (!SuperVanishHook.canSee(pl, player)) continue;
                pl.spawnParticle(data.getParticle().get(), location, amount, offX, offY, offZ, speed, options);
            }
            return;
        }
        target.getWorld().spawnParticle(data.getParticle().get(), location, amount, offX, offY, offZ, speed, options, true);
    }
}