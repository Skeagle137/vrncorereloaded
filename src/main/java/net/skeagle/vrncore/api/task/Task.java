package net.skeagle.vrncore.api.task;

import net.skeagle.vrncore.api.VRNPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Task {

    private final int id;
    private final Plugin plugin;

    private Task(Plugin plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public static Task delayed(Runnable r) {
        return delayed(VRNPlugin.getInstance(), r, 0);
    }

    public static Task delayed(Runnable r, long delay) {
        return delayed(VRNPlugin.getInstance(), r, delay);
    }

    public static Task delayed(Plugin plugin, Runnable r, long delay) {
        return new Task(plugin, Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, r, delay));
    }

    public static Task repeat(Runnable r, long period) {
        return repeat(r, 0, period);
    }

    public static Task repeat(Runnable r, long delay, long period) {
        return repeat(VRNPlugin.getInstance(), r, delay, period);
    }

    public static Task repeat(Plugin plugin, Runnable r, long delay, long period) {
        return new Task(plugin, Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, r, delay, period));
    }

    public static Task delayedAsync(Runnable r) {
        return asyncDelayed(VRNPlugin.getInstance(), r, 0);
    }

    public static Task delayedAsync(Runnable r, long delay) {
        return asyncDelayed(VRNPlugin.getInstance(), r, delay);
    }

    @SuppressWarnings("deprecation")
    public static Task asyncDelayed(Plugin plugin, Runnable r, long delay) {
        return new Task(plugin, Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, r, delay));
    }

    public static Task repeatAsync(Runnable r, long period) {
        return repeatAsync(VRNPlugin.getInstance(), r, 0, period);
    }

    public static Task repeatAsync(Runnable r, long delay, long period) {
        return repeatAsync(VRNPlugin.getInstance(), r, delay, period);
    }

    @SuppressWarnings("deprecation")
    public static Task repeatAsync(Plugin plugin, Runnable r, long delay, long period) {
        return new Task(plugin, Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, r, delay, period));
    }

    public boolean isQueued() {
        return Bukkit.getScheduler().isQueued(id);
    }

    public boolean isRunning() {
        return Bukkit.getScheduler().isCurrentlyRunning(id);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
