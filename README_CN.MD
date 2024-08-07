## XavierChatSync

<img src="https://krseoul.imgtbl.com/i/2024/08/05/66b0b6f413380.jpeg" alt="logo" width="100" height="100">

_一个用于同步 Minecraft 服务器聊天与 QQ 群消息的插件。_

### 警告

- 本插件是 **XinxinBotApi** 的附属插件，支持更多类型的Q群消息转发。
- 请在使用本插件之前，先行配置好**前置**[XinxinBotApi](https://bbs.mcxin.cn/archives/216)。

### 功能特点

- **双向消息同步**：支持将 Minecraft 服务器的聊天信息同步到 QQ 群，同时也可以将 QQ 群的消息同步到 Minecraft 服务器。
- **自定义加入/离开信息**：当玩家加入或离开服务器时，可发送自定义消息至 QQ 群。
- **多类型消息支持**：能够处理 QQ 群中的**图片**、**表情**、**文件**、**回复**、**合并转发**、**@** 等多种类型的消息。
- **模组适配**：支持与其他模组适配，如 [ChatImage](https://modrinth.com/mod/chatimage/) 等，用于让Java版玩家在服内实时查看照片。

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

- `/xavierchatsync reload`：重新加载插件的配置文件。

> 可简写为 `/xcs reload`

### 配置文件

```yaml
config-version: 1.0.0
groups:
  - "965325092"
join_msg: "玩家 %player_name% 加入了服务器"
quit_msg: "玩家 %player_name% 离开了服务器"
group_chat_msg: "<%player_name%>: {MSG}"
#{QQ}: QQ号码, {NICK}: QQ昵称, {CARD}: 群昵称，{PLAYER}: QQ绑定的玩家（未绑定则显示群昵称）
game_chat_msg: "&7[QQ群] {CARD}: {MSG}"
future:
  supported_ChatImage: true
  # 启用此模式后，服务器内的图片信息会解析为 ChatImage 模组能解析的图片信息并转发至服务器中。安装此模组的玩家可在服务器内显示图片。
debug: false
bstats: true
```

### 统计信息

[![](https://bstats.org/signatures/bukkit/XavierChatSync.svg)](https://bstats.org/plugin/bukkit/XavierChatSync/22894)

**如有任何问题或建议，欢迎联系作者或提交 Issue。**
