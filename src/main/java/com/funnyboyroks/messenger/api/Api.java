package com.funnyboyroks.messenger.api;

import com.funnyboyroks.messenger.Messenger;
import com.funnyboyroks.messenger.data.Message;

public class Api {

    /**
     * Add a message which will be saved into the plugin's `messages.json` file and loaded again when the server
     * starts.
     *
     * @param message The message, in MiniMessage format
     */
    public void addSavedMessage(String message) {
        Messenger.dataHandler().addMessage(message);
        Messenger.dataHandler().save();
        Messenger.messageHandler().messageQueue().add(new Message(message));
    }

    /**
     * Add a message to the list of rotating messages ingame.  The message will exist until the server is stopped,
     * reloaded, or the plugin is reload using `/messages reload`.
     * <p>
     * Note: this will not immediately show in game, for that {@link Api#updateMessages} must be called.
     *
     * @param message The message to append
     */
    public void addMessage(Message message) {
        Messenger.dataHandler().addMessage(message);
    }

    /**
     * Add a message to the list of rotating messages ingame.  The message will exist until the server is stopped,
     * reloaded, or the plugin is reload using `/messages reload`.
     * <p>
     * Note: this will not immediately show in game, for that {@link Api#updateMessages} must be called.
     *
     * @param message       The message to append
     * @param appendToQueue Whether the message should be appended to the current message queue
     */
    public void addMessage(Message message, boolean appendToQueue) {
        Messenger.dataHandler().addMessage(message);
        if (appendToQueue) {
            Messenger.messageHandler().messageQueue().add(message);
        }
    }

    /**
     * Update the message queue for the rotating messages.  This will reset the queue to the beginning and load any
     * messages that have been added with {@link Api#addMessage} or {@link Api#addSavedMessage(String)}
     */
    public void updateMessages() {
        Messenger.messageHandler().updateQueue();
    }

}
