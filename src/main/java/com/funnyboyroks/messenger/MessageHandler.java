package com.funnyboyroks.messenger;

import com.funnyboyroks.messenger.data.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.Queue;

public class MessageHandler {

    private BukkitTask     task;
    private Queue<Message> messageQueue;

    public MessageHandler() {
    }

    public void updateQueue() {
        this.messageQueue = new LinkedList<>(Messenger.dataHandler().messages());
    }

    public void start() {
        this.updateQueue();
        this.task = Bukkit.getScheduler().runTaskTimer(Messenger.instance(), () -> {
            Message top = this.messageQueue.poll();

            // queue is empty, so we ignore
            if (top == null) return;

            this.broadcast(top);

            // Add to the end of the queue so that we can have an endless loop
            this.messageQueue.add(top);
        }, 20L, Messenger.config().duration * 20L);
    }

    /**
     * Stop the message handler from announcing any more messages
     *
     * @return whether it was started before this method was called
     */
    public boolean stop() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
            return true;
        }
        return false;
    }

    /**
     * Reload the message handler.
     * <p>
     * This is equivalent to calling {@link MessageHandler#stop} and {@link MessageHandler#start}
     */
    public void reload() {
        this.stop();
        this.start();
    }

    public void broadcast(Message message) {
        Bukkit.getOnlinePlayers()
            .stream()
            .filter(message.playerFilter())
            .forEach(p -> {
                Component msg = message.generator().apply(p);
                Component msg2 = MiniMessage.miniMessage()
                    .deserialize(Messenger.config().messageFormat)
                    .replaceText(b -> b.matchLiteral("%message%").replacement(msg));
                p.sendMessage(msg2);
            });
    }

    public Queue<Message> messageQueue() {
        return this.messageQueue;
    }

}
