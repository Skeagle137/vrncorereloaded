package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.trail.VRNParticle;
import net.skeagle.vrncore.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TrailsGUI extends Menu {

    private final Button ArrowButton;
    private final Button ResetButton;
    private final Button PlayerButton;
    private final Player player;

    public TrailsGUI(final Player p) {
        player = p;
        PlayerButton = new ButtonMenu(new PlayerTrailSelection(player), CompMaterial.BLAZE_POWDER,
                "&b&lPlayer trail selection menu",
                "",
                "&7Click to open the player",
                "&7trail selection menu.");
        ResetButton = new Button() {
            @Override
            public void onClickedInMenu(final Player p, final Menu menu, final ClickType click) {
                final PlayerData data = PlayerManager.getData(player.getUniqueId());
                if (click.isLeftClick()) {
                    if (data.getPlayertrail() == null) {
                        animateTitle("&cNo trail to reset");
                        return;
                    }
                    animateTitle("&5Player trail reset");
                    data.setPlayertrail(null);
                }
                if (click.isRightClick()) {
                    if (data.getArrowtrail() == null) {
                        animateTitle("&cNo trail to reset");
                        return;
                    }
                    animateTitle("&5Arrow trail reset");
                    data.setArrowtrail(null);
                }
                if (click.isShiftClick() && click.isLeftClick()) {
                    if (data.getPlayertrail() == null) {
                        animateTitle("&cNo player trail active to configure");
                        return;
                    }
                    //new TrailsGUI.OptionsMenu(false).displayTo(p);
                }
                if (click.isShiftClick() && click.isRightClick()) {
                    if (data.getArrowtrail() == null) {
                        animateTitle("&cNo arrow trail active to configure");
                        return;
                    }
                    //new TrailsGUI.OptionsMenu(true).displayTo(p);
                }
                restartMenu();
            }

            @Override
            public ItemStack getItem() {
                final PlayerData data = PlayerManager.getData(player.getUniqueId());
                return ItemUtil.genItem(Material.REDSTONE,
                        "&6&lReset trails",
                        "",
                        "&7Player trail: " + (data.getPlayertrail() != null ? "&a" + data.getPlayertrail() : "&cnone"),
                        "&7Arrow trail: " + (data.getArrowtrail() != null ? "&a" + data.getArrowtrail() : "&cnone"),
                        "&bLeft click &7to reset player trail.",
                        "&aShift left click &7to edit player trail.",
                        "&dRight click &7to reset arrow trail.",
                        "&9Shift right click &7to edit arrow trail.")
                        .glint(data.getArrowtrail() != null || data.getPlayertrail() != null).build();
            }
        };
        ArrowButton = new ButtonMenu(new ArrowTrailSelection(player), CompMaterial.ARROW,
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
        private final Player player;

        private ArrowTrailSelection(final Player p) {
            super(null, Arrays.stream(VRNParticle.values()).collect(Collectors.toList()));
            setSize(9 * 4);
            setTitle("&9Arrow Trails");
            this.player = p;


            back = new Button() {
                @Override
                public void onClickedInMenu(final Player p, final Menu menu, final ClickType clickType) {
                    new TrailsGUI(player).displayTo(p);
                }

                @Override
                public ItemStack getItem() {
                    return ItemUtil.genItem(Material.ARROW,
                            "&e&lBack to main menu").build();
                }
            };

            info = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                }

                @Override
                public ItemStack getItem() {
                    return ItemUtil.genItem(Material.MAP,
                            "&3&lInstructions",
                            "",
                            "&bClick to apply an available trail.",
                            "&bMiddle click to see the trail",
                            "&boptions customization menu.").build();
                }
            };

        }

        @Override
        protected ItemStack convertToItemStack(final VRNParticle particle) {
            final PlayerData data = PlayerManager.getData(player.getUniqueId());

            if (getViewer().hasPermission("vrn.arrowtrails." + particle.name().toLowerCase())) {
                return ItemUtil.genItem(particle.getMaterial(),
                        "&6" + particle.getParticleName(),
                        "", "&7Click to select",
                        "&r&7this arrow trail.")
                        .glint(data.getArrowtrail() != null && data.getArrowtrail() == particle.getParticle()).build();
            }
            return ItemUtil.genItem(Material.GRAY_DYE,
                    "&4???",
                    "", "&cYou cannot use",
                    "&cthis arrow trail.").build();
        }

        @Override
        protected void onPageClick(final Player p, final VRNParticle particle, final ClickType click) {
            if (getViewer().hasPermission("vrn.arrowtrails." + particle.name().toLowerCase())) {
                animateTitle("&2Arrow trail successfully set.");
                final PlayerData data = PlayerManager.getData(player.getUniqueId());
                data.setArrowtrail(particle.getParticle());
            } else
                animateTitle("&4You cannot use this trail.");
            restartMenu();
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
        private final Player player;

        private PlayerTrailSelection(final Player p) {
            super(null, Arrays.stream(VRNParticle.values()).collect(Collectors.toList()));
            setSize(9 * 4);
            setTitle("&dPlayer Trails");
            this.player = p;

            back = new Button() {
                @Override
                public void onClickedInMenu(final Player p, final Menu menu, final ClickType clickType) {
                    new TrailsGUI(player).displayTo(p);
                }

                @Override
                public ItemStack getItem() {
                    return ItemUtil.genItem(Material.ARROW,
                            "&e&lBack to main menu").build();
                }
            };

            info = new Button() {
                @Override
                public void onClickedInMenu(final Player p, final Menu menu, final ClickType clickType) {
                }

                @Override
                public ItemStack getItem() {
                    return ItemUtil.genItem(Material.MAP,
                            "&3&lInstructions",
                            "",
                            "&bClick to apply an available trail.",
                            "&bMiddle click to see the trail",
                            "&boptions customization menu.").build();
                }
            };
        }

        @Override
        protected ItemStack convertToItemStack(final VRNParticle particle) {
            final PlayerData data = PlayerManager.getData(player.getUniqueId());

            if (getViewer().hasPermission("vrn.playertrails." + particle.name().toLowerCase())) {
                return ItemUtil.genItem(particle.getMaterial(),
                        "&6" + particle.getParticleName(),
                        "", "&7Click to select",
                        "&r&7this player trail.")
                        .glint(data.getPlayertrail() != null && data.getPlayertrail() == particle.getParticle()).build();
            }
            return ItemUtil.genItem(Material.GRAY_DYE,
                    "&4???",
                    "", "&cYou cannot use",
                    "&cthis player trail.").build();
        }

        @Override
        protected void onPageClick(final Player p, final VRNParticle particle, final ClickType click) {
            if (getViewer().hasPermission("vrn.playertrails." + particle.name().toLowerCase())) {
                animateTitle("&2Player trail successfully set.");
                final PlayerData data = PlayerManager.getData(player.getUniqueId());
                data.setPlayertrail(particle.getParticle());
            } else
                animateTitle("&4You cannot use this trail.");
            restartMenu();
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

    /*

    private final class OptionsMenu extends Menu {

        private final Button color;
        private final Button info;
        private final Button style;
        private final Button back;

        OptionsMenu(final boolean arrow) {
            color = new Button() {
                @Override
                public void onClickedInMenu(final Player p, final Menu menu, final ClickType click) {
                }

                @Override
                public ItemStack getItem() {
                    final PlayerData data = PlayerManager.getData(getViewer());
                    return ItemUtil.genItem(Material.RED_DYE,
                            "&6&lReset trails",
                            "",
                            "&bLeft click &7to reset player trail.",
                            "&dRight click &7to reset arrow trail.")
                            .glint(data.getArrowtrail() != null || data.getPlayertrail() != null).build();
                }
            };
            style = new ButtonMenu(new OptionsColorMenu(), CompMaterial.FEATHER,
                    "&d&lArrow trail selection menu",
                    "",
                    "&7Click to open the arrow",
                    "&7trail selection menu.");
            info = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                }

                @Override
                public ItemStack getItem() {
                    return ItemUtil.genItem(Material.MAP,
                            "&3&lInstructions",
                            "",
                            "&bClick the color button to change",
                            "&bthe color of the trail. Click the style",
                            "&bbutton to change how the trail",
                            "&bmoves around the " + (arrow ? "arrow" : "player") + "in game.")
                            .build();
                }
            };
            back = new ButtonMenu(new TrailsGUI(), CompMaterial.ARROW,
                    "&e&lBack to main menu");
        }

        @Override
        public ItemStack getItemAt(final int slot) {

            if (slot == 2)
                return color.getItem();

            if (slot == 4)
                return info.getItem();

            if (slot == 6)
                return style.getItem();

            if (slot == 8)
                return back.getItem();

            return null;
        }

        @Override
        protected String[] getInfo() {
            return null;
        }
    }

    private final class OptionsColorMenu extends MenuPagged<ChatColor> {

        private OptionsColorMenu() {
            super(Arrays.stream(ChatColor.values()).filter(ChatColor::isColor).collect(Collectors.toList()));
            setTitle("&9Change Trail Color");
        }

        @Override
        protected ItemStack convertToItemStack(final ChatColor color) {
            return ItemCreator.ofWool(CompColor.fromChatColor(color)).name(color + ItemUtil.bountifyCapitalized(color.name()) + "trail color").build().make();
        }

        @Override
        protected void onPageClick(final Player player, final ChatColor color, final ClickType clickType) {
            final PlayerData data = PlayerManager.getData(p);

            //data.setArrowoptions();
            animateTitle(color + "Changed color to " + ItemUtil.bountifyCapitalized(color.name()));
        }

        @Override
        protected String[] getInfo() {
            return new String[]{
                    "Click a color to use it",
                    "in your chat messages."
            };
        }
    }
    */

    /*
    private List<VRNParticle> getColorableTrails() {
        return new ArrayList<>(Arrays.asList(
                VRNParticle.POTION,
                VRNParticle.POTION_SPLASH,
                VRNParticle.NOTE,
                VRNParticle.REDSTONE
        ));
    }

     */
}
