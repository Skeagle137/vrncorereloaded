package net.skeagle.vrncore.commands;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.skeagle.vrncore.GUIs.ExpTradeGUI;
import net.skeagle.vrncore.GUIs.TrailsGUI;
import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import net.skeagle.vrnlib.commandmanager.Messages;
import net.skeagle.vrnlib.itemutils.ItemUtils;
import net.skeagle.vrnlib.misc.EventListener;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class MiscCommands {

    public MiscCommands() {
        Task.syncRepeating(() -> {
            ArmorStand stand;
            for (final UUID uuid : PlayerSitUtil.getSitMap().values()) {
                stand = (ArmorStand) Bukkit.getEntity(uuid);
                if (!stand.getPassengers().isEmpty()) {
                    final EntityArmorStand entitystand = ((CraftArmorStand) stand).getHandle();
                    entitystand.yaw = stand.getPassengers().get(0).getLocation().getYaw();
                    if (VRNUtil.getStandingBlock(stand.getLocation().add(0, 1.7, 0)) == null) {
                        final PlayerSitUtil p = PlayerSitUtil.getPlayer((Player) stand.getPassengers().get(0));
                        p.setSitting(false);
                    }
                }
            }
        }, 0, 1);

        new EventListener<>(PlayerDeathEvent.class, e ->
                handleSit(e.getEntity()));

        new EventListener<>(PlayerQuitEvent.class, e ->
                handleSit(e.getPlayer()));

        new EventListener<>(PlayerTeleportEvent.class, e ->
                handleSit(e.getPlayer()));

        new EventListener<>(PlayerArmorStandManipulateEvent.class, e -> {
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
        final Player trailsPlayer = target != null && target != player ? target : player;
        new TrailsGUI(player, trailsPlayer);
    }

    @CommandHook("sit")
    public void onSit(final Player player) {
        final PlayerSitUtil p = PlayerSitUtil.getPlayer(player);
        if (p.isSitting())
            p.setSitting(false);
        else if (Math.abs(player.getVelocity().getY()) < 0.5 && VRNUtil.getStandingBlock(player.getLocation()) != null)
            p.setSitting(true);
        else
            say(player, Messages.msg("sitAir"));
    }

    @CommandHook("exptrade")
    public void onExpTrade(final Player player) {
        new ExpTradeGUI(player);
    }

    @CommandHook("rename")
    public void onRename(final Player player, final String name) {
        ItemUtils.rename(player.getInventory().getItemInMainHand(), color(name));
        say(player, "Item successfully renamed.");
    }

    @CommandHook("rtp")
    public void onRtp(final Player player) {
        say(player, "&aSearching for a destination.");
        final Random r = new Random();
        final double x, z, origin_x, origin_z;
        final int min_x, max_x, min_z, max_z;
        min_x = Settings.Rtp.min_x;
        max_x = Settings.Rtp.max_x;
        min_z = Settings.Rtp.min_z;
        max_z = Settings.Rtp.max_z;
        origin_x = Settings.Rtp.x;
        origin_z = Settings.Rtp.z;
        final Location loc;
        x = r.nextInt(min_x + (max_x - min_x));
        z = r.nextInt(min_z + (max_z - min_z));
        loc = new Location(player.getWorld(), origin_x + x + 0.5, 200, origin_z + z + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch());

        if (player.getWorld().getEnvironment() == World.Environment.NETHER)
            loc.setY((player.getWorld().getHighestBlockYAt(loc) - 4));
        for (int i = loc.getBlockY(); i > 10; --i) {
            loc.setY(i);
            final Location one_up = loc.clone().add(0, 1, 0);
            final Location one_down = loc.clone().subtract(0, 1, 0);
            final Location two_down = loc.clone().subtract(0, 2, 0);
            if (loc.getBlock().getType().isAir() && one_up.getBlock().getType().isAir() &&
                    checkBlock(one_down.getBlock()) && checkBlock(two_down.getBlock())) {
                player.teleport(loc);
                say(player, "You have been teleported to &a" + loc.getX() + "&7, &a" + loc.getY() + "&7, &a" + loc.getZ() + "&7.");
                return;
            }
        }
        say(player, "&cCould not find a suitable location to teleport to.");
    }

    private boolean checkBlock(final Block b) {
        return b.getType().isSolid() &&
                b.getType() != Material.LAVA &&
                !b.isLiquid() && !b.isPassable();
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
