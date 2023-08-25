package com.funnyboyroks.messenger.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A representation for a message which will be broadcast to online players.
 *
 * @param generator    A generator which will take a {@link Player} and return a {@link Component} to show to that
 *                     player
 * @param playerFilter A filter which will take a {@link Player} and return whether that player should see the message
 */
public record Message(
    Function<Player, Component> generator,
    Predicate<Player> playerFilter
) {

    private static final Predicate<Player> ALL = p -> true;
    private static final MiniMessage       MM  = MiniMessage.miniMessage();

    /**
     * A representation for a message which will be broadcast to online players.
     *
     * @param generator A generator which will take a {@link Player} and return a {@link Component} to show to that
     *                  player
     */
    public Message(Function<Player, Component> generator) {
        this(generator, ALL);
    }

    /**
     * @param componentSupplier A supplier to get a component
     * @param filter            A filter which will take a {@link Player} and return whether that player should see the
     *                          message
     */
    public Message(Supplier<Component> componentSupplier, Predicate<Player> filter) {
        this(p -> componentSupplier.get(), filter);
    }

    /**
     * @param componentSupplier A supplier to get a component
     */
    public Message(Supplier<Component> componentSupplier) {
        this(componentSupplier, ALL);
    }

    /**
     * @param component The component which will be shown
     * @param filter    A filter which will take a {@link Player} and return whether that player should see the message
     */
    public Message(Component component, Predicate<Player> filter) {
        this(() -> component, filter);
    }

    /**
     * @param component The component which will be shown to all players
     */
    public Message(Component component) {
        this(() -> component);
    }

    /**
     * Take a message, replace all instances of {@code %player%} with the player's name, and then parse it as
     * MiniMessage
     *
     * @param message The message to do replacements and parsing
     */
    public Message(String message) {
        this((p) -> {
            String s = message.replace("%player%", p.getName());
            return MM.deserialize(s);
        });
    }
}
