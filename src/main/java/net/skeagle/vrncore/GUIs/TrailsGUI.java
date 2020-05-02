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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TrailsGUI extends Menu {

    private final Button ArrowButton;
    private final Button ResetButton;
    private final Button PlayerButton;

    public TrailsGUI() {
        PlayerButton = new ButtonMenu(new PlayerTrailSelection(), CompMaterial.BLAZE_POWDER,
                "&b&lPlayer trail selection menu",
                "",
                "&7Click to open the player",
                "&7trail selection menu.");
        ResetButton = new Button() {
            @Override
            public void onClickedInMenu(final Player p, final Menu menu, final ClickType click)
            {
                final PlayerCache cache = PlayerCache.getCache(p);
                    if (click.isLeftClick()) {
                        if (cache.getPlayertrail() == null) {
                            animateTitle("&cNo trail to reset");
                            return;
                        }
                        animateTitle("&5Player trail reset");
                        cache.setPlayertrail(null);
                    }
                    if (click.isRightClick()) {
                        if (cache.getArrowtrail() == null) {
                            animateTitle("&cNo trail to reset");
                            return;
                        }
                        animateTitle("&5Arrow trail reset");
                        cache.setArrowtrail(null);
                    }
                if (click.isRightClick() && click.isShiftClick()) {

                }
                    restartMenu();
            }

            @Override
            public ItemStack getItem() {
                final PlayerCache cache = PlayerCache.getCache(getViewer());
                return ItemCreator.of(CompMaterial.REDSTONE,
                        "&6&lReset trails",
                        "",
                        "&7Player trail: " + (cache.getPlayertrail() != null ? "&a" + cache.getPlayertrail() : "&cnone"),
                        "&7Arrow trail: " + (cache.getArrowtrail() != null ? "&a" + cache.getArrowtrail() : "&cnone"),
                        "&bLeft click &7to reset player trail.",
                        "&dRight click &7to reset arrow trail.")
                        .glow(cache.getArrowtrail() != null || cache.getPlayertrail() != null)
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

        private final Button back;
        private final Button info;

        private ArrowTrailSelection() {
            super(null, Arrays.stream(VRNParticle.values()).collect(Collectors.toList()));
            setSize(9 * 3);
            setTitle("&9Arrow Trails");


            back = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                    new TrailsGUI().displayTo(player);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.ARROW,
                            "&e&lBack to main menu").build().make();
                }
            };

            info = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.MAP,
                            "&3&lInstructions",
                            "",
                            "&bClick to apply an available trail.",
                            "&bMiddle click to see the trail",
                            "&boptions customization menu.").build().make();
                }
            };

        }

        @Override
        protected ItemStack convertToItemStack(final VRNParticle particle) {
            final PlayerCache cache = PlayerCache.getCache(getViewer());

            if (getViewer().hasPermission("vrn.arrowtrails." + particle.name().toLowerCase())) {
                return ItemCreator.of(particle.getMaterial(),
                        "&6" + particle.getParticleName(),
                        "", "&7Click to select",
                        "this arrow trail.")
                        .glow(cache.getArrowtrail() != null && cache.getArrowtrail() == particle.getParticle())
                        .build().make();
            }
            return ItemCreator.of(CompMaterial.GRAY_DYE,
                    "&4???",
                    "", "&cYou cannot use",
                    "&cthis arrow trail.").build().make();
        }

        @Override
        protected void onPageClick(final Player p, final VRNParticle particle, final ClickType click) {
            if (getViewer().hasPermission("vrn.arrowtrails." + particle.name().toLowerCase())) {
                animateTitle("&2Arrow trail successfully set.");
                final PlayerCache cache = PlayerCache.getCache(p);
                cache.setArrowtrail(particle.getParticle());
                restartMenu();
            } else {
                animateTitle("&4You cannot use this trail.");
                restartMenu();
            }
        }


        @Override
        public ItemStack getItemAt(final int slot) {

            if (slot == getSize() - 1)
                return back.getItem();

            if (slot == getSize() - 2)
                return info.getItem();

            return super.getItemAt(slot);
        }


        @Override
        protected String[] getInfo() {
            return null;
        }
    }

    private final class PlayerTrailSelection extends MenuPagged<VRNParticle> {

        private final Button back;
        private final Button info;

        private PlayerTrailSelection() {
            super(null, Arrays.stream(VRNParticle.values()).collect(Collectors.toList()));
            setSize(9 * 3);
            setTitle("&dPlayer Trails");

            back = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                    new TrailsGUI().displayTo(player);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.ARROW,
                            "&e&lBack to main menu").build().make();
                }
            };

            info = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.MAP,
                            "&3&lInstructions",
                            "",
                            "&bClick to apply an available trail.",
                            "&bMiddle click to see the trail",
                            "&boptions customization menu.").build().make();
                }
            };
        }

        @Override
        protected ItemStack convertToItemStack(final VRNParticle particle) {
            final PlayerCache cache = PlayerCache.getCache(getViewer());

            if (getViewer().hasPermission("vrn.playertrails." + particle.name().toLowerCase())) {
                return ItemCreator.of(particle.getMaterial(),
                        "&6" + particle.getParticleName(),
                        "", "&7Click to select",
                        "this player trail.")
                        .glow(cache.getPlayertrail() != null && cache.getPlayertrail() == particle.getParticle())
                        .build().make();
            }
            return ItemCreator.of(CompMaterial.GRAY_DYE,
                    "&4???",
                    "", "&cYou cannot use",
                    "&cthis player trail.").build().make();
        }

        @Override
        protected void onPageClick(final Player p, final VRNParticle particle, final ClickType click) {
            if (getViewer().hasPermission("vrn.playertrails." + particle.name().toLowerCase())) {
                animateTitle("&2Player trail successfully set.");
                final PlayerCache cache = PlayerCache.getCache(p);
                cache.setPlayertrail(particle.getParticle());
                restartMenu();
            } else {
                animateTitle("&4You cannot use this trail.");
                restartMenu();
            }
        }

        @Override
        public ItemStack getItemAt(final int slot) {

            if (slot == getSize() - 1)
                return back.getItem();

            if (slot == getSize() - 2)
                return info.getItem();

            return super.getItemAt(slot);
        }

        @Override
        protected String[] getInfo() {
            return null;
        }
    }

    private List<VRNParticle> getColorableTrails() {
        return new ArrayList<>(Arrays.asList(
                VRNParticle.POTION,
                VRNParticle.POTION_SPLASH,
                VRNParticle.NOTE,
                VRNParticle.REDSTONE
        ));
    }
}
