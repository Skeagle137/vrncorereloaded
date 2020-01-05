package net.skeagle.vrncore.listeners;

import net.skeagle.vrncore.utils.NickNameUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static net.skeagle.vrncore.utils.VRNUtil.color;

public class NickListener implements Listener
{
    private final NickNameUtil util;

    public NickListener(final NickNameUtil util) {
        this.util = util;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final String nick = util.getNickName(e.getPlayer().getUniqueId());
        if (nick != null) {
            e.getPlayer().setDisplayName(color(nick + "&r"));
            e.getPlayer().setPlayerListName(color(nick + "&r"));
            final String joinMsg = e.getJoinMessage();
            if (joinMsg != null && !joinMsg.equals("")) {
                e.setJoinMessage(joinMsg.replaceAll(e.getPlayer().getName(), nick));
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final String nick = util.getNickName(e.getPlayer().getUniqueId());
        if (nick != null) {
            final String quitMsg = e.getQuitMessage();
            if (quitMsg != null && !quitMsg.equals("")) {
                e.setQuitMessage(quitMsg.replaceAll(e.getPlayer().getName(), nick + ChatColor.RESET + ChatColor.YELLOW));
            }
        }
    }
}
