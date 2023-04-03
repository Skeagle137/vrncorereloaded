package net.skeagle.vrncore.commands;

import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncore.VRNCore;
import net.skeagle.vrncore.npc.NpcData;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class NpcCommands {

    @CommandHook("createnpc")
    public void onCreateNpc(final Player player, final String name) {
        if (VRNCore.getInstance().getNpcManager().getNpc(name) != null) {
            VRNUtil.say(player, "&cThat npc name already exists.");
            return;
        }
        final NpcData npc = VRNCore.getInstance().getNpcManager().createNPC(name, player);
        Bukkit.getOnlinePlayers().forEach(npc::createNPCForPlayer);
        VRNUtil.say(player, "&7NPC created.");
    }

    @CommandHook("deletenpc")
    public void ondeleteNpc(final Player player, final NpcData npc) {
        VRNCore.getInstance().getNpcManager().deleteNpc(npc);
        VRNUtil.say(player, "&7NPC removed.");
    }
}
