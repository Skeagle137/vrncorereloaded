package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Heal extends SimpleCommand {

    public Heal() {
        super("heal");
        setDescription("Heal another player or yourself.");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        if (args.length < 1) {
            checkConsole();
            final Player p = getPlayer();
            checkPerm("vrn.heal.self");
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            p.setFoodLevel(20);
            p.setFireTicks(0);
            say(p, "Your health and hunger are now full.");
            return;
        }
        checkPerm("vrn.heal.others");
        final Player a = findPlayer(args[0], VRNUtil.noton);
        a.setHealth(a.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        a.setFoodLevel(20);
        a.setFireTicks(0);
        say(a, "Your health and hunger are now full.");
        say(getSender(), "&a" + a.getName() + "&7's health and hunger are now full.");
    }
}
