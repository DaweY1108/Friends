package me.dawey.friends.commands;

import com.velocitypowered.api.command.SimpleCommand.Invocation;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.dawey.friends.Friends;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.database.PlayerDatabase;
import me.dawey.friends.utils.MessageManager;

public class FriendToggleCommand {
   private static ProxyServer proxy = Friends.getProxyServer();
   private static MessageConfig messages = ConfigManager.getMessages();

   public static void execute(Invocation invocation) {
      Player p = (Player)invocation.source();
      String[] args = (String[])invocation.arguments();
      if (PlayerDatabase.requestEnabled(p.getUsername())) {
         PlayerDatabase.toggleRequest(p.getUsername());
         p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-toggle-disabled")));
         MessageManager.log(messages.getString("log-toggle-disabled").replace("{player}", p.getUsername()));
      } else {
         PlayerDatabase.toggleRequest(p.getUsername());
         p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-toggle-enabled")));
         MessageManager.log(messages.getString("log-toggle-enabled").replace("{player}", p.getUsername()));
      }
   }
}
