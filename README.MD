## XavierChatSync

<img src="https://krseoul.imgtbl.com/i/2024/08/05/66b0b6f413380.jpeg" alt="logo" width="100" height="100">

_A powerful plugin for synchronizing chat messages between Minecraft servers and QQ groups._

[中文](README_CN.MD)|[English](README.MD)

### Warning

- This plugin is an extension of **XinxinBotApi**, supporting more types of QQ group message forwarding.
- Before using this plugin, please ensure that the **XinxinBotApi** [is properly configured](https://bbs.mcxin.cn/archives/216).

### Features

- **Two-way Message Synchronization**: Supports syncing chat messages from Minecraft servers to QQ groups and vice versa.
- **Custom Join/Leave Messages**: Sends custom messages to QQ groups when players join or leave the server.
- **Multi-type Message Support**: Handles various types of QQ group messages, including **images**, **emojis**, **files**, **replies**, **merged forwards**, **@mentions**, and more.
- **Mod Compatibility**: Compatible with other mods like [ChatImage](https://modrinth.com/mod/chatimage/), allowing Java Edition players to view images in real-time within the server.

### Dependencies

- **XinxinBotAPI**: [Click Here](https://bbs.mcxin.cn/archives/216)
- **PlaceholderAPI**: [Click Here](https://www.spigotmc.org/resources/placeholderapi.6245/)

### Screenshots

![.png](https://s2.loli.net/2024/08/06/fkdsNhADUqpQ6En.png)
![.png](https://s2.loli.net/2024/08/06/tv8RukbgYVwncdD.png)
![.png](https://s2.loli.net/2024/08/06/Uyq8dHAQzjr4Tax.png)
![.png](https://s2.loli.net/2024/08/06/1lwJ6xtRQsnD3GE.png)
![.png](https://s2.loli.net/2024/08/06/2eQsTKUmHd1F3WJ.png)
![.png](https://s2.loli.net/2024/08/06/TvsdNhcPr528B6X.png)
![.png](https://s2.loli.net/2024/08/06/HaJoLlZVkhGXwer.png)
![.png](https://s2.loli.net/2024/08/06/yXBf7QehJRNcDdA.png)
![.png](https://s2.loli.net/2024/08/06/YOIn5VrswkDeazR.png)

## Commands

- `/xavierchatsync reload`: Reloads the plugin configuration file.

> Shortened command: `/xcs reload`

### Configuration File

```yaml
config-version: 2.0.0
groups:
  - "965325092"
join_msg: "玩家%player_name%加入了服务器"
quit_msg: "玩家%player_name%离开了服务器"
group_chat_msg: "<%player_name%>: {MSG}"
game_chat_msg: "&7[QQ群] {CARD}: {MSG}"
future:
  supported_ChatImage: true
  qq2mc_message_truncation:
    enabled: true
    max_length: 30
    truncation_notice: "[原消息过长已自动截断]"
  mc2qq_message_truncation:
    enabled: true
    max_length: 30
    truncation_notice: "[原消息过长已自动截断]"
debug: false
bstats: true
```

### Statistics

[![](https://bstats.org/signatures/bukkit/XavierChatSync.svg)](https://bstats.org/plugin/bukkit/XavierChatSync/22894)

**If you have any questions or suggestions, feel free to contact the author or submit an issue.**

### Special Thanks

This plugin is developed based on **xinxinchatsync v1.0.2**. Special thanks to the original author for their strong support.

![.jpg](https://s2.loli.net/2024/08/15/1qG5ZMKARvnlBgC.jpg)