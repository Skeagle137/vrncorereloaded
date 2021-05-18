package net.skeagle.vrncore.commands;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.skeagle.vrncore.GUIs.TrailsGUI;
import net.skeagle.vrncore.GUIs.exptrade.ExpTradeGUI;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import net.skeagle.vrnlib.commandmanager.Messages;
import net.skeagle.vrnlib.misc.EventListener;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class MiscCommands {

    public MiscCommands() {
        Task.syncRepeating(() -> {
            ArmorStand stand;
            for (final UUID uuid : PlayerSitUtil.getSitMap().values()) {
                stand = (ArmorStand) Bukkit.getEntity(uuid);
                if (!stand.getPassengers().isEmpty()) {
                    final EntityArmorStand entitystand = ((CraftArmorStand) stand).getHandle();
                    entitystand.yaw = stand.getPassengers().get(0).getLocation().getYaw();
                    if (VRNUtil.getBlockExact(stand.getLocation().add(0, 1.7, 0)) == null) {
                        final PlayerSitUtil p = PlayerSitUtil.getPlayer((Player) stand.getPassengers().get(0));
                        p.setSitting(false);
                    }
                }
            }
        }, 0, 1);

        new EventListener<>(VRNcore.getInstance(), PlayerDeathEvent.class,
                e -> handleSit(e.getEntity()));

        new EventListener<>(VRNcore.getInstance(), PlayerQuitEvent.class,
                e -> handleSit(e.getPlayer()));

        new EventListener<>(VRNcore.getInstance(), PlayerTeleportEvent.class,
                e -> handleSit(e.getPlayer()));

        new EventListener<>(VRNcore.getInstance(), PlayerArmorStandManipulateEvent.class, e -> {
            if (PlayerSitUtil.containsStand(e.getRightClicked()))
                e.setCancelled(true);
        });
    }

    @CommandHook("craft")
    public void onCraft(final Player player) {
        player.openWorkbench(null, true);
    }

    @CommandHook("trails")
    public void onTrails(final Player player, final Player target) {
        if (target != null) {
            new TrailsGUI(target).displayTo(player);
            return;
        }
        new TrailsGUI(player).displayTo(player);
    }

    @CommandHook("sit")
    public void onSit(final Player player) {
        final PlayerSitUtil p = PlayerSitUtil.getPlayer(player);
        if (p.isSitting())
            p.setSitting(false);
        else if (Math.abs(player.getVelocity().getY()) < 0.5 && VRNUtil.getBlockExact(player.getLocation()) != null)
            p.setSitting(true);
        else
            say(player, Messages.msg("sitAir"));
    }

    @CommandHook("exptrade")
    public void onExpTrade(final Player player) {
        final ExpTradeGUI exp = new ExpTradeGUI(player);
        exp.open(player);
    }

    private void handleSit(final Player p) {
        final PlayerSitUtil util = PlayerSitUtil.getPlayer(p);
        if (util.isSitting())
            util.setSitting(false);
    }

    private static class PlayerSitUtil {

        private final Player p;

        private static final HashMap<UUID, UUID> sitMap = new HashMap<>();

        public static boolean containsStand(final ArmorStand stand) {
            return sitMap.containsValue(stand.getUniqueId());
        }

        public static PlayerSitUtil getPlayer(final Player p) {
            return new PlayerSitUtil(p);
        }

        public static HashMap<UUID, UUID> getSitMap() {
            return sitMap;
        }

        public PlayerSitUtil(final Player p) {
            this.p = p;
        }

        public boolean isSitting() {
            return sitMap.containsKey(p.getUniqueId());
        }

        public void setSitting(final boolean b) {
            if (b && !isSitting()) {
                final Location loc = p.getLocation();
                final ArmorStand stand = loc.getWorld().spawn(loc.clone().subtract(0.0, 1.7, 0.0), ArmorStand.class);
                stand.setGravity(false);
                stand.setVisible(false);
                stand.setSilent(true);
                say(p, "You are now sitting.");
                stand.addPassenger(p);
                sitMap.put(p.getUniqueId(), stand.getUniqueId());
            } else if (!b && isSitting()) {
                final ArmorStand stand = (ArmorStand) Bukkit.getEntity(sitMap.get(p.getUniqueId()));
                say(p, "You are no longer sitting.");
                sitMap.remove(p.getUniqueId());
                p.eject();
                p.teleport(stand.getLocation().clone().add(0.0, 1.7, 0.0));
                stand.remove();
            }
        }
    }
}
