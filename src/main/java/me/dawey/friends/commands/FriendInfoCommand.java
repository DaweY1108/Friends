package me.dawey.friends.commands;

import com.velocitypowered.api.command.SimpleCommand.Invocation;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.dawey.friends.Friends;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.database.FriendDatabase;
import me.dawey.friends.database.PlayerDatabase;
import me.dawey.friends.utils.MessageManager;

public class FriendInfoCommand {
   private static ProxyServer proxy = Friends.getProxyServer();
   private static MessageConfig messages = ConfigManager.getMessages();

   public static void execute(Invocation invocation) {
      Player p = (Player)invocation.source();
      String[] args = (String[])invocation.arguments();
      if (!p.getUsername().equalsIgnoreCase(args[1])) {
         if (FriendDatabase.areFriends(p.getUsername(), args[1])) {
            if (PlayerDatabase.getLastOnline(args[1]) == 0L) {
               SimpleDateFormat dateFormat = new SimpleDateFormat(messages.getString("friend-info-date-format"));
               Date dateSince = new Date(FriendDatabase.getFriendsSince(p.getUsername(), args[1]));

               for (String message : messages.getStringList("friend-info")) {
                  if (message.contains("{player}")) {
                     message = message.replace("{player}", args[1]);
                  }

                  if (message.contains("{startdate}")) {
                     message = message.replace("{startdate}", dateFormat.format(dateSince));
                  }

                  if (message.contains("{lastonlinedate}")) {
                     if (proxy.getPlayer(args[1]).isEmpty()) {
                        message = message.replace("{lastonlinedate}", messages.getString("friend-info-no-last-online"));
                     } else {
                        message = message.replace("{lastonlinedate}", messages.getString("friend-info-online"));
                     }
                  }

                  p.sendMessage(MessageManager.convert(message));
               }

               MessageManager.log(messages.getString("log-info").replace("{player1}", p.getUsername()).replace("{player2}", args[1]));
            } else {
               SimpleDateFormat dateFormat = new SimpleDateFormat(messages.getString("friend-info-date-format"));
               Date dateLast = new Date(PlayerDatabase.getLastOnline(args[1]));
               Date dateSince = new Date(FriendDatabase.getFriendsSince(p.getUsername(), args[1]));

               for (String message : messages.getStringList("friend-info")) {
                  if (message.contains("{player}")) {
                     message = message.replace("{player}", args[1]);
                  }

                  if (message.contains("{startdate}")) {
                     message = message.replace("{startdate}", dateFormat.format(dateSince));
                  }

                  if (message.contains("{lastonlinedate}")) {
                     if (proxy.getPlayer(args[1]).isEmpty()) {
                        message = message.replace("{lastonlinedate}", dateFormat.format(dateLast));
                     } else {
                        message = message.replace("{lastonlinedate}", messages.getString("friend-info-online"));
                     }
                  }

                  p.sendMessage(MessageManager.convert(message));
               }

               MessageManager.log(messages.getString("log-info").replace("{player1}", p.getUsername()).replace("{player2}", args[1]));
            }
         } else {
            p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("not-friends").replace("{player}", args[1])));
         }
      } else {
         p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-info-yoursef")));
      }
   }
}
