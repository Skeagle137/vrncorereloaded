package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.utils.VRNParticle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TrailsGUI extends Menu {

    private final Button ArrowButton;
    private final Button ResetButton;
    private final Button PlayerButton;

    public TrailsGUI() {
        PlayerButton = new Button() {
            @Override
            public void onClickedInMenu(final Player p, final Menu menu, final ClickType click){}

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.BLAZE_POWDER,
                        "&b&lPlayer trail selection menu",
                        "",
                        "&cCurrently unavailable.").build().make();
            }
        };
        ResetButton = new Button() {
            @Override
            public void onClickedInMenu(final Player p, final Menu menu, final ClickType click)
            {
                PlayerCache cache = PlayerCache.getCache(p);
                    if (click.isLeftClick()) {
                        //if (cache.getPlayertrail() == null) {
                        //    animateTitle("&cNo trail to reset");
                        //    return;
                        //}
                        //animateTitle("&5Player trail reset");
                        //cache.setPlayertrail(null);
                    }
                    if (click.isRightClick()) {
                        if (cache.getArrowtrail() == null) {
                            animateTitle("&cNo trail to reset");
                            return;
                        }
                        animateTitle("&5Arrow trail reset");
                        cache.setArrowtrail(null);
                    }
                    restartMenu();
            }

            @Override
            public ItemStack getItem() {
                PlayerCache cache = PlayerCache.getCache(getViewer());
                return ItemCreator.of(CompMaterial.REDSTONE,
                        "&6&lReset trails",
                        "",
                        "&7Player trail: &cnone",
                        "&7Arrow trail: " + (cache.getArrowtrail() != null ? "&a" + cache.getArrowtrail() : "&cnone"),
                        "&bLeft click &7to reset player trail.",
                        "&dRight click &7to reset arrow trail.")
                        .glow(cache.getArrowtrail() != null /*||cache.getPlayertrail() != null*/)
                        .build().make();
            }
        };
        ArrowButton = new ButtonMenu(new ArrowTrailSelection(), CompMaterial.ARROW,
                "&d&lArrow trail selection menu",
                "",
                "&7Click to open the arrow",
                "&7trail selection menu.");
    }

    @Override
    public ItemStack getItemAt(final int slot) {

        if (slot == 9 + 1)
            return PlayerButton.getItem();

        if (slot == 9 + 4)
            return ResetButton.getItem();

        if (slot == 9 + 7)
            return ArrowButton.getItem();

        return null;
    }

    @Override
    protected String[] getInfo() {
        return null;
    }

    private final class ArrowTrailSelection extends MenuPagged<VRNParticle> {

        private ArrowTrailSelection() {
            super(9 * 3, null, Arrays.stream(VRNParticle.values()).filter(VRNParticle::isUsable).collect(Collectors.toList()));
            setTitle("&9Arrow Trails");

        }

        @Override
        protected ItemStack convertToItemStack(final VRNParticle particle) {
            PlayerCache cache = PlayerCache.getCache(getViewer());

            return ItemCreator.of(particle.getMaterial(),
                    "&6" + particle.getParticleName(),
            "", "&7Click to select",
                            "this arrow trail.")
                    .glow(cache.getArrowtrail() != null && cache.getArrowtrail() == particle.getParticle())
                    .build().make();
        }

        @Override
        protected void onPageClick(final Player p, final VRNParticle particle, final ClickType click) {
            animateTitle("&2Arrow trail successfully set");
            PlayerCache cache = PlayerCache.getCache(p);
            cache.setArrowtrail(particle.getParticle());
            restartMenu();
        }

        @Override
        protected String[] getInfo() {
            return null;
        }
    }
}
