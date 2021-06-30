package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.rewards.Reward;
import net.skeagle.vrncore.rewards.RewardAction;
import net.skeagle.vrncore.rewards.RewardType;
import net.skeagle.vrnlib.inventorygui.InventoryGUI;
import net.skeagle.vrnlib.inventorygui.ItemButton;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import net.skeagle.vrnlib.misc.ChatPrompt;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.misc.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

public class RewardsGUI {

    public RewardsGUI(final Reward reward, final Player player) {
        final InventoryGUI gui = new InventoryGUI(45, "&5&lReward Editor");
        gui.addButton(10, new ItemButton() {
            @Override
            public ItemStack getItem() {
                final String message = reward.getMessage();
                return new ItemBuilder(Material.WRITABLE_BOOK).setName("&bMessage").setLore("", message != null ? message : "&8Click to set a message.").glint(message != null);
            }

            @Override
            public void onClick(final InventoryClickEvent e) {
                player.closeInventory();
                ChatPrompt.prompt(player, "&aType the message you would like to set in the chat.",
                        (c) -> {
                            sayNoPrefix(player, "&aUpdated the reward's message.");
                            Task.syncDelayed(() -> new RewardsGUI(reward, player));
                        });
            }
        });

        gui.addButton(12, new ItemButton() {
            @Override
            public ItemStack getItem() {
                final ItemBuilder builder = new ItemBuilder(Material.DIAMOND).setName("&dTime").setLore("");
                if (reward.getType() == RewardType.TIMED && reward.getTime() == 0)
                    builder.addLore("&8Click to change the time required to trigger the reward.");
                else
                    builder.addLore("&8");
                return builder;
            }

            @Override
            public void onClick(final InventoryClickEvent e) {
                player.closeInventory();
                final String s = reward.getType() == RewardType.TIMED ? "time" : "cost";
                ChatPrompt.prompt(player, "&aType the " + s + " that you would like to set in the chat.",
                        (c) -> {
                            if (reward.getType() == RewardType.TIMED) {
                                final long l;
                                try {
                                    l = TimeUtil.parseTimeString(c);
                                } catch (final TimeUtil.TimeFormatException ex) {
                                    sayNoPrefix(player, ex.getMessage());
                                    return;
                                }
                                reward.setTime(l);
                            } else
                                reward.setCost(Long.parseLong(c));
                            sayNoPrefix(player, "&aUpdated the reward's " + s + ".");
                            Task.syncDelayed(() -> new RewardsGUI(reward, player));
                        });
            }
        });

        gui.addButton(14, new ItemButton() {
            @Override
            public ItemStack getItem() {
                return new ItemBuilder(Material.FIREWORK_ROCKET).setName("&eFirework Effect").setLore("", reward.isFirework() ? "&aEnabled" : "&cDisabled");
            }

            @Override
            public void onClick(final InventoryClickEvent e) {
                reward.setFirework(!reward.isFirework());
                gui.update();
            }
        });

        gui.addButton(16, new ItemButton() {
            @Override
            public ItemStack getItem() {
                final String title = reward.getTitle();
                final String subtitle = reward.getSubtitle();
                return new ItemBuilder(Material.BIRCH_SIGN).setName("&aTitle and Subtitle")
                        .setLore("", title != null ? title : "&8Left click to set the title.", subtitle != null ? subtitle : "&8Right click to set the subtitle.")
                        .glint(title != null || subtitle != null);
            }

            @Override
            public void onClick(final InventoryClickEvent e) {
                player.closeInventory();
                final String s = e.isLeftClick() ? "title" : "subtitle";
                ChatPrompt.prompt(player, "&aType the " + s + " that you would like to set in the chat.",
                        (c) -> {
                            sayNoPrefix(player, "&aUpdated the reward's " + s + ".");
                            if (e.isLeftClick())
                                reward.setTitle(c);
                            else
                                reward.setSubtitle(c);
                            Task.syncDelayed(() -> new RewardsGUI(reward, player));
                        });
            }
        });

        gui.addButton(29, new ItemButton() {
            @Override
            public ItemStack getItem() {
                return new ItemBuilder(Material.PAPER).setName("&cRequires Permission").setLore("", reward.hasPermission() ? "&aYes" : "&cNo");
            }

            @Override
            public void onClick(final InventoryClickEvent e) {
                reward.setPermission(!reward.hasPermission());
                gui.update();
            }
        });

        gui.addButton(31, new ItemButton() {
            @Override
            public ItemStack getItem() {
                return new ItemBuilder(Material.EMERALD_BLOCK).setName("&9Reward Type").setLore("", reward.getType() == RewardType.TIMED ? "&aTimed" : "&bPriced");
            }

            @Override
            public void onClick(final InventoryClickEvent e) {
                reward.setFirework(!reward.isFirework());
                gui.update();
            }
        });

        gui.addButton(33, new ItemButton() {
            @Override
            public ItemStack getItem() {
                final boolean b = HookManager.isLuckPermsLoaded();
                final RewardAction action = reward.getAction();
                final ItemBuilder builder = new ItemBuilder(Material.BEACON).setName("&6Trigger Action").setLore("", !b ? "&cYour server must have Luckperms to set trigger actions."
                        : action != null ? action.getName() : "&8Left click to cycle through trigger actions.").glint(action != null && b);
                if (action == null && b) {
                    builder.addLore("&8Right click to set the target group/track name.");
                }
                if (action != null && reward.getGroup() != null && b) {
                    builder.addLore("&8Shift click to reset and have no trigger action.");
                }
                return builder;
            }

            @Override
            public void onClick(final InventoryClickEvent e) {
                if (!HookManager.isLuckPermsLoaded()) return;
                if (e.isShiftClick()) {
                    reward.setAction(null);
                    reward.setGroup(null);
                    gui.update();
                } else {
                    final RewardAction action = reward.getAction();
                    if (e.isLeftClick()) {
                        reward.setAction(reward.getAction() == null ? RewardAction.values()[0] : reward.getAction().next());
                        gui.update();
                    } else {
                        player.closeInventory();
                        final String s = action.appliesToTrack() ? "track" : "group";
                        final RewardAction finalAction = action;
                        ChatPrompt.prompt(player, "&aType the " + s + " that you would like this trigger action to apply to in the chat.",
                                (c) -> {
                                    sayNoPrefix(player, "&aUpdated the reward's trigger action.");
                                    reward.setAction(finalAction);
                                    reward.setGroup(c);
                                    Task.syncDelayed(() -> new RewardsGUI(reward, player));
                                });
                    }
                }
            }
        });

        gui.open(player);
    }
}
