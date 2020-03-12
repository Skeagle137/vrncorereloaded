package net.skeagle.vrncore.commands.tpa;

import net.skeagle.vrncore.utils.TPAUtil;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Tpaccept extends SimpleCommand {

    private final TPAUtil util = TPAUtil.getUtil();

    public Tpaccept() {
        super("tpaccept");
        setDescription("Accept a pending teleport request.");
        setPermission("vrn.tpaccept");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        final Player p = getPlayer();
        if (!util.hasRequest(p)) {
            say(p, "&cYou do not have any pending teleport requests.");
            return;
        }
        final Player a = util.getStoredPlayer(p);
        checkNotNull(a, VRNUtil.noton);
        say(p, "&aTeleport request accepted.");
        say(a, "&aTeleport request accepted.");
        util.teleportPlayer(p, a);
    }
}
