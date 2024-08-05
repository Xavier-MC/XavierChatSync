package xaviermc.top.xavierChatSync;

import com.xinxin.BotApi.BotAction;
import com.xinxin.BotApi.BotBind;
import com.xinxin.BotEvent.GroupMessageEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bukkit.Bukkit;
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

import java.util.List;

import static org.apache.commons.lang.StringUtils.replace;

public class XavierChatSync extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("XavierChatSync Loading");
    }

    @Override
    public void onDisable() {
        getLogger().info("XavierChatSync Unloaded");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            String msg = getConfig().getString("join_msg");
            if (msg != null && !msg.isEmpty()) {
                msg = PlaceholderAPI.setPlaceholders(event.getPlayer(), msg);
                sendGroupMessages(msg);
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
            }
        });
    }

    @EventHandler
    public void onGroup(GroupMessageEvent event) {
        if (getConfig().getLongList("groups").contains(event.getGroup_id())) {
            String msg = getConfig().getString("game_chat_msg");
            if (msg != null) {
                msg = msg.replaceAll("&", "__color__");
                String json = event.getJson();
                JSONObject jsonObject = JSONObject.fromObject(json);
                String rawMessage = jsonObject.optString("raw_message", "");
                String textMessage = parseMessage(jsonObject, event);

                if (!msg.trim().isEmpty() && !textMessage.trim().isEmpty()) {
                    msg = formatMessage(event, msg, textMessage);
                    Bukkit.broadcastMessage(msg);
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
                }
            } catch (Exception e) {
                if (getConfig().getBoolean("debug")) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendGroupMessages(String msg) {
        List<Long> groups = getConfig().getLongList("groups");
        for (Long group : groups) {
            BotAction.sendGroupMessage(group, msg, new boolean[]{true});
        }
    }

    private String parseMessage(JSONObject jsonObject, GroupMessageEvent event) {
        StringBuilder textMessage = new StringBuilder();
        String imageUrl = null;
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
                    case "redbag":
                        textMessage.append("[红包]");
                        break;
                    case "forward":
                        textMessage.append("[合并转发]");
                        break;
                    case "record":
                        textMessage.append("[语音消息]");
                        break;
                    case "viedo":
                        textMessage.append("[短视频]");
                        break;
                    case "music":
                        textMessage.append("[音乐]");
                        break;
                    case "at":
                        String qq = messageObject.getJSONObject("data").optString("qq", "");
                        String cardName = getCardNameFromQQ(event, qq);
                        textMessage.append("@").append(cardName);
                        break;
                }
            }
        }
        return textMessage.toString();
    }

    private String getCardNameFromQQ(GroupMessageEvent event, String qq) {
        String playerName = BotBind.getBindPlayerName(qq);
        if (playerName == null) {
            return event.getSender_card();
        }
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
        if (binder != null) {
            msg = PlaceholderAPI.setPlaceholders(binder, msg);
        }
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
        sender.sendMessage("§7[§bXavierChatSync§7] §a配置文件已经重新载入!");
        return true;
    }
}