package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.warps.WarpManager;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

public class setwarp extends SimpleCommand {

    public setwarp() {
        super("setwarp");
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Create a warp.");
        setPermission("vrn.setwarp");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        List<Integer> list = new ArrayList<>();
        for (PermissionAttachmentInfo perminfo : getPlayer().getEffectivePermissions()) {
            if (perminfo.getPermission().contains("vrn.warplimit.")) {
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
        if (WarpManager.getInstance().getWarpsOwnedByPlayer(getPlayer()).size() >= maxsize && !hasPerm("vrn.warplimit.*"))
            returnTell("&cYou can only set a maximum of " + maxsize + " warps. Delete some of your warps if you want to set more.");
        if (WarpManager.getInstance().setWarp(getPlayer(), args[0]))
            returnTell("&7Warp set, teleport to it with &a/warp " + args[0] + "&7.");
        returnTell("&cA warp with that name already exists.");
    }
}
