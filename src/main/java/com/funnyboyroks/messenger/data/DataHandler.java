package com.funnyboyroks.messenger.data;

import com.funnyboyroks.messenger.Messenger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DataHandler {

    private final Plugin plugin;
    private final Gson   gson;

    private List<String>  minimessages;
    private List<Message> messages;

    public DataHandler(Plugin plugin) {
        this.plugin = plugin;

        this.gson = new GsonBuilder()
            .disableHtmlEscaping()
            .setLenient()
            .setPrettyPrinting()
            .create();
    }

    public void load() {
        this.loadMessages();
    }

    public void save() {
        this.saveMessages();
    }

    /**
     * Add a message to the list of messages which are saved to the `messages.json` file.
     *
     * @param message The message, formatted with MiniMessage
     */
    public void addMessage(String message) {
        this.minimessages.add(message);
        this.messages.add(new Message(message));
    }

    /**
     * Add a message to the list of messages to be shown in-game
     * <p>
     * Note: this differs from {@link DataHandler#addMessage(String)} because these will <i>not</i> be saved to disk.
     */
    public void addMessage(Message message) {
        this.messages.add(message);
    }

    /**
     * @return An unmodifiable list of the messages
     */
    public List<Message> messages() {
        return Collections.unmodifiableList(this.messages);
    }

    private void loadMessages() {
        File messagesFile = new File(this.plugin.getDataFolder(), "messages.json");

        try {
            if (!messagesFile.exists()) {
                Files.writeString(messagesFile.toPath(), "[]");
            }
        } catch (IOException e) {
            Messenger.instance().getLogger().severe("Unable to write to `messages.json`");
            throw new RuntimeException(e);
        }

        try (Reader r = Files.newBufferedReader(messagesFile.toPath())) {
            TypeToken<?> t = TypeToken.getParameterized(List.class, String.class);
            this.minimessages = new ArrayList<>(this.gson.fromJson(r, t.getType()));
            this.messages = this.minimessages.stream().map(Message::new).collect(Collectors.toList());
        } catch (IOException e) {
            Messenger.instance().getLogger().severe("Unable to read from `messages.json`");
            throw new RuntimeException(e);
        }
    }

    private void saveMessages() {
        File messagesFile = new File(this.plugin.getDataFolder(), "messages.json");

        try (Writer w = Files.newBufferedWriter(messagesFile.toPath())) {
            this.gson.toJson(this.minimessages, w);
        } catch (IOException e) {
            Messenger.instance().getLogger().severe("Unable to write to `messages.json`");
            throw new RuntimeException(e);
        }
    }
}
