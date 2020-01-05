package net.skeagle.vrncore.listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static net.skeagle.vrncore.utils.VRNUtil.color;

public class SmallThings implements Listener {
    @EventHandler
    public void WorldChange(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        String world = p.getWorld().getName();
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color("&a&lCurrently in world: \"" + world + ".\"")));
    }

    public void Death(PlayerDeathEvent e) {
        Player p = e.getEntity().getPlayer();
        Location loc = p.getLocation();
        p.getWorld().playSound(loc, Sound.ENTITY_HORSE_DEATH, SoundCategory.PLAYERS,5.0F, 1.8F);
    }
    //why is this here lol
}
