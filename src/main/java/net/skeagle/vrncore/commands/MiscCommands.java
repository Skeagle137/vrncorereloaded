package net.skeagle.vrncore.commands;

import net.skeagle.vrncommands.BukkitMessages;
import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncommands.Sender;
import net.skeagle.vrncore.GUIs.ExpTradeGUI;
import net.skeagle.vrncore.GUIs.TrailsGUI;
import net.skeagle.vrncore.Settings;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.itemutils.ItemUtils;
import net.skeagle.vrnlib.misc.EventListener;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.UnaryOperator;

import static net.skeagle.vrncommands.BukkitUtils.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;
import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

public class MiscCommands {

    private final Map<UUID, UUID> lastReplies = new HashMap<>();

    @CommandHook("message")
    public void onMessage(final Player player, final Player target, String message) {
        VRNcore.getPlayerData(player.getUniqueId()).thenAcceptAsync(playerData -> {
            VRNcore.getPlayerData(target.getUniqueId()).thenAccept(targetData -> {
                String outgoing = BukkitMessages.msg("senderMsgPlayerPrefix").replaceAll("%target%", targetData.getName()) + message;
                String incoming = BukkitMessages.msg("playerMsgSenderPrefix").replaceAll("%sender%", playerData.getName())  + message;
                sayNoPrefix(player, outgoing);
                sayNoPrefix(target, incoming);
                lastReplies.put(player.getUniqueId(), target.getUniqueId());
                lastReplies.put(target.getUniqueId(), player.getUniqueId());
            });
        });
    }

    @CommandHook("reply")
    public void onReply(final Player player, String message) {
        if (lastReplies.get(player.getUniqueId()) == null) {
            say(player, "&cYou have not messaged someone to be able to reply to them yet.");
            return;
        }
        onMessage(player, Bukkit.getPlayer(lastReplies.get(player.getUniqueId())), message);
    }

    @CommandHook("craft")
    public void onCraft(final Player player) {
        player.openWorkbench(null, true);
    }

    @CommandHook("trails")
    public void onTrails(final Player player, final Player target) {
        new TrailsGUI(player, target);
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
        final int min = Settings.rtpMin;
        final int max = Settings.rtpMax;
        final int origin_x = Settings.rtpOriginX;
        final int origin_z = Settings.rtpOriginZ;
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
}
