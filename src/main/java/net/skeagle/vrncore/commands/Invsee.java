package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;


public class Invsee extends SimpleCommand {

    public Invsee() {
        super("invsee");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("View another player's inventory.");
        setPermission("vrn.invsee");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override //@SuppressWarnings("deprecation")
    protected void onCommand() {
        final Player a = findPlayerInternal(args[0]);
        if (a != null) {
            getPlayer().openInventory(a.getInventory());
            say(getPlayer(), "Now showing &a" + a.getName() + "&7's inventory.");
            return;
        }
        say(getPlayer(), VRNUtil.noton);
        /*
        Common.runAsync(() -> {
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
            File f;
            if (p.hasPlayedBefore())
                f = new File("world" + File.separator + "playerdata", p.getUniqueId().toString() + ".dat");
            else {
                say(getPlayer(), "&cno player data found for " + args[0] + ".");
                return;
            }
            try {
                NBTTagCompound nbt = NBTCompressedStreamTools.a(new FileInputStream(f));
                NBTTagList list = (NBTTagList) nbt.get("Inventory");
                if (list == null) {
                    say(getPlayer(), "&cCould not read " + p.getName() + "'s inventory.");
                    return;
                }
                List<NBTTagCompound> compoundlist = new ArrayList<>();
                for (int i = 0; i < list.size() - 1; i++) {
                    NBTTagCompound compound = (NBTTagCompound) list.get(i);
                    if (!compound.isEmpty())
                        compoundlist.add(compound);
                }
                Common.run(() -> new InvseeGUI(compoundlist, p));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

         */
    }
}
