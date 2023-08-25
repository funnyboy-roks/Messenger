package com.funnyboyroks.messenger.config;

import de.exlll.configlib.*;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.nio.file.Path;

@Configuration
public class Config {

    @Comment("Time between each message being sent (in seconds)")
    public long duration = 300;

    @Comment({
        "Format for each message when it's sent.",
        "`%message%` is replaced with the MiniMessage content"
    })
    public String messageFormat = "<gray>[<red>+</red>]</gray> %message%";

    @Comment("Whether the logging for the plugin should be verbose (useful for debugging)")
    public boolean debugLogging = false;

    public static Config load(Plugin plugin) {
        YamlConfigurationProperties properties = YamlConfigurationProperties.newBuilder()
            .header(
                "Configuration for Messenger\n" +
                "If you need any help or have any questions, feel free to ask on my Discord: https://discord.gg/qsUP2t5VpW"
            )
            .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
            .build();

        Path configFile = new File(plugin.getDataFolder(), "config.yml").toPath();

        return YamlConfigurations.update(
            configFile,
            Config.class,
            properties
        );
    }

}
