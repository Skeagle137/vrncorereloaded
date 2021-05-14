package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Back extends SimpleCommand {

    public Back() {
        super("back");
        setDescription("Teleport back to a player's previous location.");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        if (args.length < 1)
            checkPerm("vrn.back.self");
        else
            checkPerm("vrn.back.others");
        final Player p = args.length < 1 ? getPlayer() : findPlayer(args[0], VRNUtil.noton);
        final BackCache back = new BackCache();
        final Location backLoc = back.getBackLoc(getPlayer().getUniqueId());
        if (backLoc == null) {
            say(p, args.length < 1 ? "&cYou do not have anywhere to teleport back to."
                    : "&a" + p.getName() + " &7does not have a saved last location.");
            return;
        }
        final Location newLoc = getPlayer().getLocation();
        back.teleToBackLoc(getPlayer(), p);
        back.setBackLoc(getPlayer().getUniqueId(), newLoc);
        say(p, args.length < 1 ? "&7Teleported to your last location."
                : "&7Teleported to &a" + p.getName() + "&7's last location.");
    }

    private static class BackCache {
        private final Map<UUID, Location> backLoc = new HashMap<>();

        public Location getBackLoc(final UUID id) {
            return this.backLoc.get(id);
        }

        public void setBackLoc(final UUID id, final Location loc) {
            this.backLoc.remove(id);
            this.backLoc.put(id, loc);
        }

        public void teleToBackLoc(final Player p, final Player targetLoc) {
            p.teleport(backLoc.get(targetLoc.getUniqueId()));
        }
    }
}
