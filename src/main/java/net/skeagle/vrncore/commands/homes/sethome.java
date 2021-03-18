package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.homes.HomeManager;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

public class sethome extends SimpleCommand {

    public sethome() {
        super("sethome");
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Create a Home.");
        setPermission("vrn.sethome");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        List<Integer> list = new ArrayList<>();
        for (PermissionAttachmentInfo perminfo : getPlayer().getEffectivePermissions()) {
            if (perminfo.getPermission().contains("vrn.homelimit.")) {
                String[] perm = perminfo.getPermission().split("\\.");
                if (perm.length == 3) {
                    if (perm[2].equals("*"))
                        continue;
                    int i;
                    try {
                        i = Integer.parseInt(perm[2]);
                    } catch (Exception ignored) {
                        continue;
                    }
                    list.add(i);
                }
            }
        }
        int maxsize = 0;
        for (int i : list)
            if (i >= maxsize)
                maxsize = i;
        if (HomeManager.getInstance().getHomeNames(getPlayer()).size() >= maxsize && !hasPerm("vrn.homelimit.*"))
            returnTell("&cYou can only set a maximum of " + maxsize + " homes. Delete some of your homes if you want to set more.");
        if (HomeManager.getInstance().setHome(args[0], getPlayer()))
            returnTell("&7Home set, teleport to it with &a/home " + args[0] + "&7.");
        returnTell("&cA home with that name already exists.");
    }
}
