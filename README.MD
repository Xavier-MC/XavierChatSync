# XavierChatSync

<img src="https://krseoul.imgtbl.com/i/2024/08/05/66b0b6f413380.jpeg" alt="logo" width="100" height="100">

_XavierChatSync is a plugin designed to synchronize chat between a Minecraft server and QQ group messages._

## Warning

- This plugin is an auxiliary plugin for XinxinBotApi, enabling communication between group chats and server chats.
- Before using this plugin, please configure the prerequisite bot plugin [XinxinBotApi](https://bbs.mcxin.cn/archives/216).

## Features

- **Two-Way Message Synchronization**: Supports syncing chat messages from the Minecraft server to a QQ group and vice versa.
- **Custom Join/Leave Messages**: Allows sending custom messages to the QQ group when a player joins or leaves the server.
- **Multiple Message Types Supported**: Handles various message types in QQ groups, including images, emojis, files, replies, merged forwards, etc.
- **Mod Compatibility**: Compatible with other mods, such as [ChatImage](https://modrinth.com/mod/chatimage/).

## Prerequisite Plugins

- **XinxinBotAPI**: [Click Here](https://bbs.mcxin.cn/archives/216)
- **PlaceholderAPI**: [Click Here](https://www.spigotmc.org/resources/placeholderapi.6245/)

## Commands

- `/xavierchatsync reload`: Reload the plugin's configuration file.
> Alias: `/xcs reload`

## Configuration File

```yaml
config-version: 1.0.0
groups:
  - "965325092"
join_msg: "Player %player_name% has joined the server"
quit_msg: "Player %player_name% has left the server"
group_chat_msg: "<%player_name%>: {MSG}"
#{QQ}: QQ number, {NICK}: QQ nickname, {CARD}: Group nickname, {PLAYER}: QQ bound player (displays group nickname if not bound)
game_chat_msg: "&7[QQ Group] {CARD}: {MSG}"
future:
  supported_ChatImage: true
  # When this mode is enabled, image information within the server will be parsed into a format that the ChatImage mod can interpret and forwarded to the server. Players with this mod installed will be able to display images in the server.
debug: false
bstats: true
```

## Statistics

[![](https://bstats.org/signatures/bukkit/XavierChatSync.svg)](https://bstats.org/plugin/bukkit/XavierChatSync/22894)

**For any questions or suggestions, feel free to contact the author or submit an issue.**