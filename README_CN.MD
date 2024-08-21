## XavierChatSync

<img src="https://krseoul.imgtbl.com/i/2024/08/05/66b0b6f413380.jpeg" alt="logo" width="100" height="100">

_一个用于同步 Minecraft 服务器聊天与 QQ 群消息的强大插件。_

[中文](README_CN.MD)|[English](README.MD)

### 警告

- 本插件是 **XinxinBotApi** 的附属插件，支持更多类型的 QQ 群消息转发。
- 在使用本插件前，请先配置好**前置插件** [XinxinBotApi](https://bbs.mcxin.cn/archives/216)。

### 功能特点

- **双向消息同步**：支持将 Minecraft 服务器的聊天信息同步到 QQ 群，反之亦然。
- **自定义加入/离开信息**：当玩家加入或离开服务器时，可以向 QQ 群发送自定义消息。
- **多类型消息支持**：能够处理 QQ 群中的**图片**、**表情**、**文件**、**回复**、**合并转发**、**@** 等多种类型消息。
- **模组适配**：支持与其他模组适配，如 [ChatImage](https://modrinth.com/mod/chatimage/)，Java版玩家可以在游戏内实时查看图片。

### 前置插件

- **XinxinBotAPI**：[点击前往](https://bbs.mcxin.cn/archives/216)
- **PlaceholderAPI**：[点击前往](https://www.spigotmc.org/resources/placeholderapi.6245/)

### 图片展示

![.png](https://s2.loli.net/2024/08/06/fkdsNhADUqpQ6En.png)
![.png](https://s2.loli.net/2024/08/06/tv8RukbgYVwncdD.png)
![.png](https://s2.loli.net/2024/08/06/Uyq8dHAQzjr4Tax.png)
![.png](https://s2.loli.net/2024/08/06/1lwJ6xtRQsnD3GE.png)
![.png](https://s2.loli.net/2024/08/06/2eQsTKUmHd1F3WJ.png)
![.png](https://s2.loli.net/2024/08/06/TvsdNhcPr528B6X.png)
![.png](https://s2.loli.net/2024/08/06/HaJoLlZVkhGXwer.png)
![.png](https://s2.loli.net/2024/08/06/yXBf7QehJRNcDdA.png)
![.png](https://s2.loli.net/2024/08/06/YOIn5VrswkDeazR.png)

## 指令

- `/xavierchatsync reload`：重新加载插件配置文件。

> 简写：`/xcs reload`

### 配置文件

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

### 统计信息

[![](https://bstats.org/signatures/bukkit/XavierChatSync.svg)](https://bstats.org/plugin/bukkit/XavierChatSync/22894)

**如有任何问题或建议，欢迎联系作者或提交 Issue。**

### 特别鸣谢

本插件基于**xinxinchatsync v1.0.2版本**开发，特别感谢原作者的大力支持。

![.jpg](https://s2.loli.net/2024/08/15/1qG5ZMKARvnlBgC.jpg)
