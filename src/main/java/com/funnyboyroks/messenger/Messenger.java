package com.funnyboyroks.messenger;

import com.funnyboyroks.messenger.api.Api;
import com.funnyboyroks.messenger.command.CommandMessenger;
import com.funnyboyroks.messenger.config.Config;
import com.funnyboyroks.messenger.data.DataHandler;
import com.funnyboyroks.messenger.event.MessengerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Messenger extends JavaPlugin {

    private static Messenger instance;
    private final DataHandler    dataHandler;
    private final MessageHandler messageHandler;
    private       Config config;
    private final Api    api;

    public Messenger() {
        instance = this;
        this.api = new Api();
        this.dataHandler = new DataHandler(this);
        this.messageHandler = new MessageHandler();
    }

    public static Messenger instance() {
        return instance;
    }

    public static Config config() {
        return instance().config;
    }

    public static Api api() {
        return instance().api;
    }

    public static DataHandler dataHandler() {
        return instance().dataHandler;
    }

    public static MessageHandler messageHandler() {
        return instance().messageHandler;
    }

    public static Logger logger() {
        return instance().getLogger();
    }

    @Override
    public void onEnable() {
        this.load(false);

        this.getCommand("messenger").setExecutor(new CommandMessenger());
    }

    @Override
    public void onDisable() {
        this.dataHandler.save();
    }

    public void load(boolean reload) {
        this.config = Config.load(this);
        this.dataHandler.load();
        this.getServer().getPluginManager().callEvent(new MessengerLoadEvent(reload));
        this.messageHandler.reload();
    }

    public void reload() {
        this.load(true);
    }

}
