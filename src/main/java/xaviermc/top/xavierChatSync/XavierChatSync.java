package xaviermc.top.xavierChatSync;

import com.xinxin.BotApi.BotAction;
import com.xinxin.BotApi.BotBind;
import com.xinxin.BotEvent.GroupMessageEvent;
import java.util.Iterator;

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

public class XavierChatSync extends JavaPlugin implements Listener {

    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("XavierChatSync Loading...");
    }

    public void onDisable() {
        this.getLogger().info("XavierChatSync Unloaded");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            String msg = this.getConfig().getString("join_msg");
            if (!msg.isEmpty()) {
                msg = PlaceholderAPI.setPlaceholders(event.getPlayer(), msg);
                Iterator<Long> iterator = this.getConfig().getLongList("groups").iterator();

                while(iterator.hasNext()) {
                    long group = iterator.next();
                    BotAction.sendGroupMessage(group, msg, new boolean[]{true});
                }
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            String msg = this.getConfig().getString("quit_msg");
            if (!msg.isEmpty()) {
                msg = PlaceholderAPI.setPlaceholders(event.getPlayer(), msg);
                Iterator<Long> iterator = this.getConfig().getLongList("groups").iterator();

                while(iterator.hasNext()) {
                    long group = iterator.next();
                    BotAction.sendGroupMessage(group, msg, new boolean[]{true});
                }
            }
        });
    }

    @EventHandler
    public void onGroup(GroupMessageEvent event) {
        if (this.getConfig().getLongList("groups").contains(event.getGroup_id())) {
            String msg = this.getConfig().getString("game_chat_msg");
            msg = msg.replaceAll("&", "__color__");
            String json = event.getJson();
            JSONObject jsonObject = JSONObject.fromObject(json);
            String rawMessage = jsonObject.optString("raw_message", "");
            String textMessage = "";

            // 解析 message 数组并提取 text
            JSONArray messageArray = jsonObject.optJSONArray("message");
            if (messageArray != null) {
                for (int i = 0; i < messageArray.size(); i++) {
                    JSONObject messageObject = messageArray.getJSONObject(i);
                    String type = messageObject.optString("type");
                    if (type.equals("text")) {
                        textMessage += messageObject.getJSONObject("data").optString("text", "");
                    } else if (type.equals("image")) {
                        textMessage += "[图片]";
                    } else if (type.equals("file")) {
                        textMessage += "[文件]";
                    } else if (type.equals("face")) {
                        textMessage += "[表情]";
                    }else if (type.equals("reply")) {
                        textMessage += "回复 " + messageObject.getJSONObject("data").optString("text", "");
                    }
                }
            }

            if (!msg.trim().isEmpty() && !textMessage.trim().isEmpty()) {
                msg = msg.replace("{QQ}", "" + event.getUser_id());
                msg = msg.replace("{NICK}", event.getSender_nickname());
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
                msg = msg.replace("???", event.getSender_nickname());

                msg = msg.replace("{MSG}", textMessage);

                Bukkit.broadcastMessage(msg);
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onChat(AsyncPlayerChatEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                if (event.isCancelled()) {
                    return;
                }

                Player player = event.getPlayer();
                String msg = this.getConfig().getString("group_chat_msg");
                if (msg.trim().isEmpty()) {
                    return;
                }

                msg = msg.replace("{MSG}", formatMsg(event.getMessage()));
                msg = PlaceholderAPI.setPlaceholders(player, msg);
                msg = msg.replaceAll("§\\S", "");
                Iterator<Long> iterator = this.getConfig().getLongList("groups").iterator();

                // 向每个群组发送消息
                while(iterator.hasNext()) {
                    long group = iterator.next();
                    BotAction.sendGroupMessage(group, msg, new boolean[]{true});
                }
            } catch (Exception var7) {
                if (this.getConfig().getBoolean("debug")) {
                    var7.printStackTrace();
                }
            }
        });
    }

    public static String formatMsg(String str) {
        return str.replace("&", "&amp;").replace("[", "&#91;").replace("]", "&#93;");
    }

    // 命令处理
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.reloadConfig();
        sender.sendMessage("§7[§bXavierChatSync§7] §a配置文件已经重新载入!"); // 发送消息给命令发送者
        return true;
    }
}