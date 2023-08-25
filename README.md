# Messenger

A plugin to send
[MiniMessage](https://docs.advntr.dev/minimessage/format.html) formatted
rotating messages in chat on a scheduled interval.

## Configuration

- `duration`: integer
    - Seconds between each message being sent in chat
    - Default: `300` (5 minutes)
- `message-format`: string
    - Format of the messages to be displayed to the user in chat.  This
      is used for adding a prefix or suffix to the message
    - Supports [MiniMessage](https://docs.advntr.dev/minimessage/format.html)
    - Default: `<gray>[<red>+</red>]</gray> %message%` (renders to `[+] %message%`)
- `debug-logging`: boolean
    - Whether verbose logging should be printed to the console
    - Default: `false`

## Commands

- `/messenger <add|reload>`
    - Main command for Messenger
    - `/messenger add` - Open an editor to add a new rotating message to
      the chat.
    - `/messenger add <message>` - Add a message into the chat directly
    - `/messenger reload` - Reload the plugin's configuration and
      messages from the `messages.json` file.

## Permissions

- `messenger.command.messenger`
    - Ability to run the `/messenger` command
    - Default: `op`

## Data

The plugin currently only handles two files: `config.yml` and
`messages.json`.

The `config.yml` holds the configuration for the plugin, see
[Configuration](#configuration) for more details

The `messages.json` holds each message to be printed into chat.
Removing/Adding a message from this list will remove/add it from/to the
game (After `/messenger reload` or server restart).

## Message Format

A message can be formatted using
[MiniMessage](https://docs.advntr.dev/minimessage/format.html).  If
`%player%` is included in the message, it will be replaced with the
player's username.

## API

The plugin offers a simple API which allows for adding new messages.

To add it as a dependency if using maven, add the following to your pom.xml:

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.funnyboy-roks</groupId>
    <artifactId>Messenger</artifactId>
    <version>Tag</version>
</dependency>
```

Otherwise, checkout the [jitpack repo](https://jitpack.io/#funnyboy-roks/Messenger/1.0) for other systems

### Usage example

```java
// construct a new message that says "hello " + playername
// and is sent to all players
Message msg = new Message(
    p -> Component.text("hello ").append(p.displayName()),
    p -> true
);

// Add a message to the list
Messenger.api().addMessage(msg);
```

## Future To-Do

- [ ] PAPI support - If there is interest
- [ ] More configuration options

I'm always looking for ideas, if you have any, please create an issue
and let me know!
