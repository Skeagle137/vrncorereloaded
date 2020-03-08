package net.skeagle.vrncore.commands;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Spawnmob extends SimpleCommand {

    public Spawnmob() {
        super("spawnmob");
        setMinArguments(1);
        setUsage("<mob> <x> <y> <z> <amount>");
        setDescription("Spawns a mob where the player is looking or at a specific location");
        setPermission("vrn.spawnmob");
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final EntityType entityType = findEnum(EntityType.class, args[0], "&cEntity named {enum} is invalid.");
        checkBoolean(entityType.isAlive() && entityType.isSpawnable(), "&cEntity " + entityType + " is not spawnable.");
        final Location location;
        int amount = 1;
        if (args.length == 2) {
            amount = findNumber(1, "&cPlease specify a valid amount of the mob.");
            checkBoolean(amount > 0, "&cAmount of mobs cannot be less than one.");
        }
        final Block b = getPlayer().getTargetBlock(null, 20);
        location = new Location(b.getWorld(), b.getLocation().getX(), b.getLocation().getY() + 1, b.getLocation().getZ());

        for (int i = 0; i < amount; i++) {
            location.getWorld().spawnEntity(location, entityType);
        }
        say(getPlayer(), "Spawned " + amount + " " + entityType.toString().toLowerCase() + " at " + Common.shortLocation(location));
    }

    @Override
    protected List<String> tabComplete() {
        if (isPlayer()) {
            if (args.length == 1) {
                return completeLastWord(EntityType.values());
            }
        }

        return new ArrayList<>();
    }
}
