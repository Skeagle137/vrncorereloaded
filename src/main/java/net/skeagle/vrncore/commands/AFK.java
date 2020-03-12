package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.HashMap;

import static net.skeagle.vrncore.utils.VRNUtil.color;

public class AFK extends SimpleCommand {

    private final HashMap<Player, Integer> afkList = new HashMap<>();
    private final HashMap<Player, Integer> afkAmount = new HashMap<>();

    public AFK() {
        super("afk");
        setDescription("Set yourself as afk.");
        setPermissionMessage(VRNUtil.noperm);
    }

    private boolean isAFK(final Player p) {
        if (this.afkList.containsKey(p)) {
            if ((int) p.getLocation().getYaw() == this.afkList.get(p)) {
                return true;
            }
            this.afkList.remove(p);
            this.afkAmount.remove(p);
        }
        this.afkList.put(p, (int) p.getLocation().getYaw());
        return false;
    }

    private void updatePlayer(final Player p) {
        if (this.isAFK(p)) {
            if (this.afkAmount.containsKey(p)) {
                final int timeAFK = afkAmount.get(p);
                hasPerm("vrn.bypassafk");
                if (hasPerm("vrn.afk." + timeAFK)) {
                    this.afkAmount.remove(p);
                    this.afkList.remove(p);
                    p.kickPlayer(color("&cYou have been kicked after idling for " + timeAFK + " minutes."));
                    return;
                }
                this.afkAmount.put(p, this.afkAmount.get(p) + 1);
            } else {
                this.afkAmount.put(p, 1);
            }
        }
    }

    @Override
    public void onCommand() {
        checkConsole();
    }
}
