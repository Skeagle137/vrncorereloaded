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
        ArrowButton = new ButtonMenu(new ArrowTrailSelection(), CompMaterial.ARROW,
                "&d&lArrow trail selection menu",
                "",
                "&7Click to open the arrow",
                "&7trail selection menu.");
    }

    @Override
    public ItemStack getItemAt(final int slot) {

        if (slot == 9 + 2)
            return PlayerButton.getItem();

        if (slot == 9 + 6)
            return ArrowButton.getItem();

        return null;
    }

    @Override
    protected String[] getInfo() {
        return null;
    }

    private class ArrowTrailSelection extends MenuPagged<VRNParticle> {

        private ArrowTrailSelection() {
            super(9 * 3, null, Arrays.stream(VRNParticle.values()).filter(VRNParticle::isUsable).collect(Collectors.toList()));
            setTitle("&9&lArrow Trails");

        }

        @Override
        protected ItemStack convertToItemStack(final VRNParticle particle) {
            return ItemCreator.of(particle.getMaterial()).name("&7" + particle.getParticleName()).build().make();
        }

        @Override
        protected void onPageClick(final Player p, final VRNParticle particle, final ClickType click) {
            animateTitle("&2&lArrow trail set to &a&l" + particle.getParticleName());
            PlayerCache cache = PlayerCache.getCache(p);
            cache.setArrowtrail(particle.getParticle());

        }

        @Override
        protected String[] getInfo() {
            return null;
        }
    }
}
