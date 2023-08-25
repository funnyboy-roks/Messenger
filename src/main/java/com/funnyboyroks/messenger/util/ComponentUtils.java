package com.funnyboyroks.messenger.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Contract;

public class ComponentUtils {

    /**
     * Create a component with the styling and actions for a link
     *
     * @param baseText The base text of the link
     * @param url      The link
     * @param color    The color for the link
     * @return Link Component
     */
    @Contract(pure = true)
    public static Component link(String baseText, String url, TextColor color) {
        Component c = Component.text(baseText, color, TextDecoration.UNDERLINED);
        return c.clickEvent(ClickEvent.openUrl(url))
            .hoverEvent(HoverEvent.showText(Component.text("Open Link", NamedTextColor.GRAY)));
    }

    /**
     * Create a component with the styling and actions for a link
     *
     * @param baseText The base text of the link
     * @param url      The link
     * @return Link Component
     */
    @Contract(pure = true)
    public static Component link(String baseText, String url) {
        return link(baseText, url, NamedTextColor.AQUA);
    }

    /**
     * Create a component with the styling and actions for a link
     *
     * @param url The link
     * @return Link Component
     */
    @Contract(pure = true)
    public static Component link(String url) {
        return link(url, url);
    }
}
