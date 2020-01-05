package net.skeagle.vrncore.commands.tpa;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.TPAUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Tpahere extends SimpleCommand {

    TPAUtil util = TPAUtil.getUtil();

    public Tpahere() {
        super("tpahere");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("Request another player to teleport to you.");
        setPermission("vrn.tpahere");
        setPermissionMessage(VRNcore.noperm);
    }

    public void onCommand() {
        checkConsole();
        Player p = getPlayer();
        Player a = findPlayer(args[0], VRNcore.noton);
        if (p.getName().equalsIgnoreCase(args[0])) {
            say(p, "&cYou cannot teleport to yourself.");
            return;
        }
        if (util.hasRequest(a)) {
            say(p, "&cYou already have a pending teleport request with that player.");
            return;
        }
        if (util.hasSentRequest(a)) {
            say(p, "&cThat player already has a pending request.");
            return;
        }
        util.setTpahere(true);
        say(p, "&aTeleport request sent.");
        say(a, "&a" + p.getName() + " &7is requesting for you to teleport to them. Do /tpaccept to accept the request or /tpdeny to deny it. This request will expire in 2 minutes.");
        util.addPlayers(a.getUniqueId(), p.getUniqueId());
        util.DelTPATimer(p, a, VRNcore.getInstance());
    }
}
