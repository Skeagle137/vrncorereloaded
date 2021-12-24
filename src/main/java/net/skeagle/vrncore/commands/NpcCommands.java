package net.skeagle.vrncore.commands;

import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.npc.Npc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class NpcCommands {

    @CommandHook("createnpc")
    public void onCreateNpc(final Player player, final String name) {
        if (VRNcore.getInstance().getNpcManager().getNpc(name) != null) {
            say(player, "&cThat npc name already exists.");
            return;
        }
        final Npc npc = VRNcore.getInstance().getNpcManager().createNPC(name, player);
        Bukkit.getOnlinePlayers().forEach(npc::createNPCForPlayer);
        say(player, "&7NPC created.");
    }

    @CommandHook("deletenpc")
    public void ondeleteNpc(final Player player, final Npc npc) {
        VRNcore.getInstance().getNpcManager().deleteNpc(npc);
        say(player, "&7NPC removed.");
    }
}
