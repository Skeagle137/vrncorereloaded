package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.GUIs.ExpTradeGUI;
import net.skeagle.vrncore.GUIs.TrailsGUI;
import net.skeagle.vrncore.config.Settings;
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
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class MiscCommands {

    private final Map<UUID, UUID> sitMap = new HashMap<>();

    public MiscCommands() {
        Task.syncRepeating(() -> {
            ArmorStand stand;
            for (final UUID uuid : sitMap.values()) {
                stand = (ArmorStand) Bukkit.getEntity(uuid);
                if (!stand.getPassengers().isEmpty() && stand.getPassengers().get(0) instanceof Player player) {
                    stand.setRotation(player.getLocation().getYaw(), player.getLocation().getPitch());
                    if (VRNUtil.getStandingBlock(stand.getLocation().add(0, 1.7, 0)) == null) {
                        setSitting(player, false);
                    }
                }
            }
        }, 0, 1);

        new EventListener<>(PlayerDeathEvent.class, e -> {
            if (isSitting(e.getEntity())) {
                setSitting(e.getEntity(), false);
            }
        });

        new EventListener<>(PlayerQuitEvent.class, e -> {
            if (isSitting(e.getPlayer())) {
                setSitting(e.getPlayer(), false);
            }
        });

        new EventListener<>(PlayerTeleportEvent.class, e -> {
            if (isSitting(e.getPlayer())) {
                setSitting(e.getPlayer(), false);
            }
        });

        new EventListener<>(PlayerArmorStandManipulateEvent.class, e -> {
            if (sitMap.containsValue(e.getRightClicked().getUniqueId())) {
                e.setCancelled(true);
            }
        });
    }

    @CommandHook("craft")
    public void onCraft(final Player player) {
        player.openWorkbench(null, true);
    }

    @CommandHook("trails")
    public void onTrails(final Player player, final Player target) {
        new TrailsGUI(player, target);
    }

    @CommandHook("sit")
    public void onSit(final Player player) {
        if (isSitting(player)) {
            setSitting(player, false);
        }
        else if (Math.abs(player.getVelocity().getY()) < 0.5 && VRNUtil.getStandingBlock(player.getLocation()) != null)
            setSitting(player, true);
        else
            say(player, Messages.msg("sitAir"));
    }

    @CommandHook("exptrade")
    public void onExpTrade(final Player player) {
        new ExpTradeGUI(player);
    }

    @CommandHook("rename")
    public void onRename(final Player player, final String name) {
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            say(player, "&cYou must have an item in your hand.");
            return;
        }
        final ItemStack i = ItemUtils.rename(player.getInventory().getItemInMainHand(), color(name));
        player.getInventory().setItemInMainHand(i);
        say(player, "Item successfully renamed.");
    }

    @CommandHook("rtp")
    public void onRtp(final Player player) {
        say(player, "&aSearching for a destination.");
        final Random r = new Random();
        final int min = Settings.Rtp.rtpMin;
        final int max = Settings.Rtp.rtpMax;
        final int origin_x = Settings.Rtp.rtpOriginX;
        final int origin_z = Settings.Rtp.rtpOriginZ;
        final int x = genRandom(r, max, min);
        final int z = genRandom(r, max, min);
        final Location loc = new Location(player.getWorld(), origin_x + x + 0.5, 128, origin_z + z + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch());

        if (player.getWorld().getEnvironment() == World.Environment.NETHER)
            loc.setY((player.getWorld().getHighestBlockYAt(loc) - 4));
        for (int i = loc.getBlockY(); i > 10; --i) {
            loc.setY(i);
            final Location one_up = loc.clone().add(0, 1, 0);
            final Location one_down = loc.clone().subtract(0, 1, 0);
            final Location two_down = loc.clone().subtract(0, 2, 0);
            if (loc.getBlock().getType().isAir() && one_up.getBlock().getType().isAir() && checkBlock(one_down.getBlock()) && checkBlock(two_down.getBlock())) {
                player.teleport(loc);
                say(player, "You have been teleported to &a" + loc.getX() + "&7, &a" + loc.getY() + "&7, &a" + loc.getZ() + "&7.");
                return;
            }
        }
        say(player, "&cCould not find a suitable location to teleport to.");
    }

    private int genRandom(final Random r, final int max, final int min) {
        int i = r.nextInt(max - min + 1) + min;
        if (Math.abs(i) < min) {
            if (i < 0) i = -min;
            if (i >= 0) i = min;
        }
        return i;
    }

    private boolean checkBlock(final Block b) {
        return b.getType().isSolid() &&
                b.getType() != Material.LAVA &&
                !b.isLiquid() && !b.isPassable();
    }

    private boolean isSitting(Player player) {
        return sitMap.containsKey(player.getUniqueId());
    }

    private void setSitting(final Player player, final boolean sitting) {
        if (sitting && !isSitting(player)) {
            final Location loc = player.getLocation();
            final ArmorStand stand = loc.getWorld().spawn(loc.clone().subtract(0.0, 1.7, 0.0), ArmorStand.class);
            stand.setGravity(false);
            stand.setVisible(false);
            stand.setSilent(true);
            say(player, "You are now sitting.");
            stand.addPassenger(player);
            sitMap.put(player.getUniqueId(), stand.getUniqueId());
        } else if (!sitting && isSitting(player)) {
            final ArmorStand stand = (ArmorStand) Bukkit.getEntity(sitMap.get(player.getUniqueId()));
            say(player, "You are no longer sitting.");
            sitMap.remove(player.getUniqueId());
            player.eject();
            player.teleport(stand.getLocation().clone().add(0.0, 1.7, 0.0));
            stand.remove();
        }
    }
}
