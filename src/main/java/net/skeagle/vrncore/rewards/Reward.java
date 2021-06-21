package net.skeagle.vrncore.rewards;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrnlib.misc.EventListener;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

public class Reward {
    private final String name;
    private boolean permission;
    private String message;
    private boolean firework;
    private String title;
    private String subtitle;
    private RewardType type;
    private RewardAction action;
    private String group;
    private long cost;
    private long time;
    private Player player;

    public Reward(final String name) {
        this.name = name;
        permission = false;
        firework = false;
        type = RewardType.TIMED;
        cost = 0;
        time = 0;
    }

    public Reward(final String name, final boolean permission, final String message,
                  final boolean firework, final String title, final String subtitle, final RewardType type, final RewardAction action, final String group, final long cost, final long time) {
        this.name = name;
        this.permission = permission;
        this.message = message;
        this.firework = firework;
        this.title = title;
        this.subtitle = subtitle;
        this.type = type;
        this.action = action;
        this.group = group;
        this.cost = cost;
        this.time = time;
    }

    public void save() {
        final SQLHelper db = VRNcore.getInstance().getDB();
        db.execute("DELETE FROM rewards WHERE name = ?", name);
        db.execute("INSERT INTO rewards (name, permission, message, firework, title, subtitle, type, action, groupname, cost, time) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                name, permission, message, firework, title, subtitle, type.toString(), action != null ? action.toString() : null, group, cost, time);
    }

    public void run(final Player player) {
        this.player = player;
        if (message != null) {
            sayNoPrefix(player, color(replaceVars(message)));
        }
        if (title != null || subtitle != null)
            player.sendTitle(title != null ? color(replaceVars(title)) : "", subtitle != null ? color(replaceVars(subtitle)) : "", 5, 120, 40);
        if (firework)
            Task.syncDelayed(() -> sendFirework(player.getLocation().add(0, 1, 0)));
        if (action != null && group != null)
            action.getAction().run(player, group);

    }

    private String replaceVars(String s) {
        s = s.replaceAll("%player%", player.getName());
        s = s.replaceAll("%group%", group);
        return s;
    }

    public boolean checkPerm(final Player player) {
        return !permission || player.hasPermission("vrn.rewards." + name);
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

    private void sendFirework(final Location loc) {
        final Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        final String id = "fw" + fw.getEntityId();
        new EventListener<>(EntityDamageByEntityEvent.class, (l, e) -> {
            if (e.getDamager().getType() == EntityType.FIREWORK && e.getDamager().getScoreboardTags().stream().anyMatch(t -> t.equals(id))) {
                e.setCancelled(true);
                Task.asyncDelayed(l::unregister, 40);
            }
        });
        final FireworkMeta fwmeta = fw.getFireworkMeta();
        fwmeta.setPower(5);
        fwmeta.addEffect(buildFireworkEffect());
        fw.addScoreboardTag(id);
        fw.setFireworkMeta(fwmeta);
        fw.setTicksLived(2);
        fw.detonate();
    }

    public String getName() {
        return name;
    }

    public boolean hasPermission() {
        return permission;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFirework() {
        return firework;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public RewardType getType() {
        return type;
    }

    public void setPermission(final boolean permission) {
        this.permission = permission;
    }

    public void setMessages(final String message) {
        this.message = message;
    }

    public void setFirework(final boolean firework) {
        this.firework = firework;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }

    public void setType(final RewardType type) {
        this.type = type;
    }

    public RewardAction getAction() {
        return action;
    }

    public void setAction(final RewardAction action) {
        this.action = action;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(final long cost) {
        this.cost = cost;
    }

    public long getTime() {
        return time;
    }

    public void setTime(final long time) {
        this.time = time;
    }
}
