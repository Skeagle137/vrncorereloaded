package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.warps.WarpManager;
import net.skeagle.vrncore.utils.storage.warps.WarpsManager;
import net.skeagle.vrncore.utils.storage.warps.WarpsResource;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class ConvertWarps extends SimpleCommand {

    public ConvertWarps() {
        super("convertwarps");
        setDescription("Convert VRNcore 4.4.x warps to the new format used in VRNcore 4.5.x and above.");
        setPermission("vrn.convert");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        List<WarpsManager> man = WarpsResource.getInstance().getManData();
        man.forEach(w -> WarpManager.getInstance().importOld(w));
        say(getPlayer(), "&7Converted all warps.");
    }
}
