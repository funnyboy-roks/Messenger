package com.funnyboyroks.messenger.command;

import com.funnyboyroks.messenger.Messenger;
import com.funnyboyroks.messenger.util.AdventureWebUI;
import com.funnyboyroks.messenger.util.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CommandMessenger implements TabCompleter, CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] argArr) {
        if (!sender.hasPermission("messenger.command.messenger")) {
            sender.sendMessage(Component.text("You do not have permission to run this command."));
            return true;
        }

        if (argArr.length == 0) {
            return false;
        }

        LinkedList<String> args = new LinkedList<>(Arrays.asList(argArr));

        Subcommand subcommand = Subcommand.from(args.poll());

        if (subcommand == null) {
            return false;
        }

        switch (subcommand) {
            case ADD -> {
                if (args.size() == 0) {
                    sender.sendMessage(Component.text("Creating editor session...", NamedTextColor.GRAY));
                    AdventureWebUI.createEditor("<gold>Rotating Message!", "/messenger add token:{token}")
                        .thenAccept((t) -> {
                            sender.sendMessage(
                                Component.text("Edit rotating message here:\n", NamedTextColor.GRAY)
                                    .append(ComponentUtils.link("https://webui.advntr.dev?token=" + t))
                            );
                        });
                } else {
                    String rest = String.join(" ", args);
                    if (rest.startsWith("token:")) {
                        sender.sendMessage(Component.text("Getting editor session...", NamedTextColor.GRAY));
                        AdventureWebUI.getOutput(rest.substring("token:".length()))
                            .exceptionally(ex -> {
                                Messenger.instance().getLogger().severe("Error getting editor session: " + ex.getMessage());
                                sender.sendMessage(Component.text("Error getting editor session.", NamedTextColor.RED));
                                ex.printStackTrace();
                                return null;
                            })
                            .thenAccept((res) -> {
                                if (res == null) {
                                    sender.sendMessage(Component.text("Unknown editor session.  Try saving again in the webui.", NamedTextColor.RED));
                                    return;
                                }
                                Messenger.api().addSavedMessage(res);
                                sender.sendMessage(
                                    Component.text("Added: ", NamedTextColor.GRAY)
                                        .append(MiniMessage.miniMessage().deserialize(res))
                                );
                            });
                    } else {
                        Messenger.api().addSavedMessage(rest);
                        sender.sendMessage(
                            Component.text("Added: ", NamedTextColor.GRAY)
                                .append(MiniMessage.miniMessage().deserialize(rest))
                        );
                    }
                }
            }
            case RELOAD -> {
                sender.sendMessage(Component.text("Reloading Plugin... ", NamedTextColor.GRAY));
                Messenger.instance().reload();
                sender.sendMessage(Component.text("Plugin Reloaded!", NamedTextColor.GREEN));
            }
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Subcommand.formattedValues()
                .stream()
                .filter(v -> v.startsWith(args[0].replace('-', '_').toLowerCase()))
                .toList();
        }
        return Collections.emptyList();
    }

    private enum Subcommand {
        ADD,
        //LIST,   // TODO: List the values -- have an additional option to show both those saved to the disk and session specific
        //REMOVE, // TODO: Allow items to be removed using the index that the show in `/messages list`
        RELOAD,
        ;

        public static Subcommand from(String str) {
            try {
                return Subcommand.valueOf(str.replace('-', '_').toUpperCase());
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }

        public static List<String> formattedValues() {
            return Arrays.stream(values()).map(v -> v.name().replace('_', '-').toLowerCase()).toList();
        }
    }
}
