package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Push extends SimpleCommand {

    public Push() {
        super("push");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("Pushes a player and sends them flying backwards.");
        setPermission("vrn.push");
    }

    public void onCommand() {
        Player p = findPlayer(args[0], VRNcore.noton);
        p.setVelocity(p.getLocation().getDirection().multiply(-3));
        p.setVelocity(new Vector(p.getVelocity().getX(), 1.5D, p.getVelocity().getZ()));
        say(getSender(), "&eYou pushed " + p.getName() + "!");
        //TODO: add optional arg to specify velocity
    }
}