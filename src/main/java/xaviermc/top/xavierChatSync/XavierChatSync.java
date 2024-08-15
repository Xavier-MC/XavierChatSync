package xaviermc.top.xavierChatSync;

import com.xinxin.BotApi.BotAction;
import com.xinxin.BotApi.BotBind;
import com.xinxin.BotEvent.GroupMessageEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xaviermc.top.xavierChatSync.metrics.Metrics;

import java.util.List;

public class XavierChatSync extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info(ChatColor.GREEN + "[XavierChatSync] Plugin enabled");
        String version =getConfig().getString("config-version");
        if (version == null || !version.equals("1.0.0")) {
            getLogger().warning(ChatColor.RED + "[XavierChatSync] 配置文件版本不匹配，请删除旧配置文件后重载插件");
        }
        if (getConfig().getBoolean("bstats")){
            int pluginId = 22894;
            Metrics metrics = new Metrics(this, pluginId);
            metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
        }
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "[XavierChatSync] Plugin disabled");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            String msg = getConfig().getString("join_msg");
            if (msg != null && !msg.isEmpty()) {
                msg = PlaceholderAPI.setPlaceholders(event.getPlayer(), msg);
                sendGroupMessages(msg);
                if (getConfig().getBoolean("debug")) {
                    getLogger().info(ChatColor.YELLOW + "[XavierChatSync] Player joined: " + event.getPlayer().getName());
                }
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            String msg = getConfig().getString("quit_msg");
            if (msg != null && !msg.isEmpty()) {
                msg = PlaceholderAPI.setPlaceholders(event.getPlayer(), msg);
                sendGroupMessages(msg);
                if (getConfig().getBoolean("debug")) {
                    getLogger().info(ChatColor.YELLOW + "[XavierChatSync] Player quit: " + event.getPlayer().getName());
                }
            }
        });
    }

    @EventHandler
    public void onGroup(GroupMessageEvent event) {
        if (getConfig().getLongList("groups").contains(Long.valueOf(event.getGroup_id()))) {
            String msg = getConfig().getString("game_chat_msg");
            if (msg != null) {
                msg = msg.replaceAll("&", "__color__");
                String json = event.getJson();
                JSONObject jsonObject = JSONObject.fromObject(json);
                String textMessage = parseMessage(jsonObject, event);

                if (!msg.trim().isEmpty() && !textMessage.trim().isEmpty()) {
                    msg = formatMessage(event, msg, textMessage);
                    Bukkit.broadcastMessage(msg);
                    if (getConfig().getBoolean("debug")) {
                        getLogger().info(ChatColor.YELLOW + "[XavierChatSync] Group message received: " + textMessage);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                Player player = event.getPlayer();
                String msg = getConfig().getString("group_chat_msg");
                if (msg != null && !msg.trim().isEmpty()) {
                    msg = msg.replace("{MSG}", formatMsg(event.getMessage()));
                    msg = PlaceholderAPI.setPlaceholders(player, msg);
                    msg = msg.replaceAll("§\\S", "");
                    sendGroupMessages(msg);
                    if (getConfig().getBoolean("debug")) {
                        getLogger().info(ChatColor.YELLOW + "[XavierChatSync] Player chat: " + event.getMessage());
                    }
                }
            } catch (Exception e) {
                if (getConfig().getBoolean("debug")) {
                    getLogger().severe(ChatColor.RED + "[XavierChatSync] Error processing chat event: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendGroupMessages(String msg) {
        List<Long> groups = getConfig().getLongList("groups");
        for (Long group : groups) {
            BotAction.sendGroupMessage(group, msg, new boolean[]{true});
            if (getConfig().getBoolean("debug")) {
                getLogger().info(ChatColor.YELLOW + "[XavierChatSync] Sent group message to group " + group + ": " + msg);
            }
        }
    }

    private String parseMessage(JSONObject jsonObject, GroupMessageEvent event) {
        StringBuilder textMessage = new StringBuilder();
        String imageUrl;
        JSONArray messageArray = jsonObject.optJSONArray("message");
        if (messageArray != null) {
            for (int i = 0; i < messageArray.size(); i++) {
                JSONObject messageObject = messageArray.getJSONObject(i);
                String type = messageObject.optString("type");
                switch (type) {
                    case "text":
                        textMessage.append(messageObject.getJSONObject("data").optString("text", ""));
                        break;
                    case "image":
                        if (getConfig().getBoolean("future.supported_ChatImage")) {
                            imageUrl = messageObject.getJSONObject("data").optString("url", "");
                            String tmp = "[[CICode,url=<Web Url>,name=图片]]";
                            tmp = tmp.replace("<Web Url>", imageUrl);
                            textMessage.append(tmp);
                        } else {
                            textMessage.append("[图片]");
                        }
                        break;
                    case "file":
                        textMessage.append("[文件]");
                        break;
                    case "face":
                        textMessage.append("[表情]");
                        break;
                    case "reply":
                        textMessage.append("回复 ").append(messageObject.getJSONObject("data").optString("text", ""));
                        break;
                    case "forward":
                        textMessage.append("[合并转发]");
                        break;
                    case "record":
                        textMessage.append("[语音消息]");
                        break;
                    case "video":
                        textMessage.append("[短视频]");
                        break;
                    case "at":
                        String name = messageObject.getJSONObject("data").optString("name", "");
                        if (name == null){
                            name = getCardNameFromQQ(event, messageObject.getJSONObject("data").optString("qq", ""));
                        }
                        if (name == null){
                            name = messageObject.getJSONObject("data").optString("qq", "");
                        }
                        textMessage.append("@").append(name);
                        break;
                }
            }
        }
        return textMessage.toString();
    }

    private String getCardNameFromQQ(GroupMessageEvent event, String qq) {
        String playerName = BotBind.getBindPlayerName(qq);
        return playerName;
    }

    private String formatMessage(GroupMessageEvent event, String msg, String textMessage) {
        msg = msg.replace("{QQ}", String.valueOf(event.getUser_id()));
        msg = msg.replace("{NICK}", event.getSender_nickname());
        msg = msg.replace("{CARD}", event.getSender_card());
        String playerName = BotBind.getBindPlayerName(String.valueOf(event.getUser_id()));
        OfflinePlayer binder = null;
        if (playerName == null) {
            playerName = "???";
        } else {
            binder = Bukkit.getOfflinePlayer(playerName);
        }
        msg = msg.replace("{PLAYER}", playerName);
        msg = PlaceholderAPI.setPlaceholders(binder, msg);
        msg = msg.replaceAll("§\\S", "");
        msg = msg.replace("__color__", "§");
        msg = msg.replace("???", event.getSender_card());
        msg = msg.replace("{MSG}", textMessage);
        return msg;
    }

    public static String formatMsg(String str) {
        return str.replace("&", "&amp;").replace("[", "&#91;").replace("]", "&#93;");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        reloadConfig();
        if (sender instanceof Player) {
            sender.sendMessage("§7[§bXavierChatSync§7] §a配置文件已经重新载入!");
            getLogger().info(ChatColor.YELLOW + "[XavierChatSync] Configuration reloaded by " + sender.getName());
        } else {
            sender.sendMessage("§7[§bXavierChatSync§7] §a配置文件已经重新载入!");
            getLogger().info(ChatColor.YELLOW + "[XavierChatSync] Configuration reloaded by console");
        }
        return true;
    }
}