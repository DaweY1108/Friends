package me.dawey.friends.commands;

import com.velocitypowered.api.command.SimpleCommand.Invocation;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.dawey.friends.Friends;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.database.FriendDatabase;
import me.dawey.friends.utils.MessageManager;

public class FriendAdminCommand {
   private static ProxyServer proxy = Friends.getProxyServer();
   private static MessageConfig messages = ConfigManager.getMessages();

   public static void executeAdd(Invocation invocation) {
      Player p = (Player)invocation.source();
      String[] args = (String[])invocation.arguments();
      if (!FriendDatabase.areFriends(args[2], args[3])) {
         FriendDatabase.addFriend(args[2], args[3]);
         p.sendMessage(
            MessageManager.convert(
               messages.getString("prefix-admin") + messages.getString("friend-admin-added").replace("{player1}", args[2]).replace("{player2}", args[3])
            )
         );
         MessageManager.log(messages.getString("log-admin-add").replace("{admin}", p.getUsername()).replace("{player1}", args[2]).replace("{player2}", args[3]));
      } else {
         p.sendMessage(
            MessageManager.convert(
               messages.getString("prefix-admin")
                  + messages.getString("friend-admin-already-friends").replace("{player1}", args[2]).replace("{player2}", args[3])
            )
         );
      }
   }

   public static void executeDelete(Invocation invocation) {
      Player p = (Player)invocation.source();
      String[] args = (String[])invocation.arguments();
      if (FriendDatabase.areFriends(args[2], args[3])) {
         FriendDatabase.deleteFriend(args[2], args[3]);
         p.sendMessage(
            MessageManager.convert(
               messages.getString("prefix-admin") + messages.getString("friend-admin-removed").replace("{player1}", args[2]).replace("{player2}", args[3])
            )
         );
         MessageManager.log(
            messages.getString("log-admin-remove").replace("{admin}", p.getUsername()).replace("{player1}", args[2]).replace("{player2}", args[3])
         );
      } else {
         p.sendMessage(
            MessageManager.convert(
               messages.getString("prefix-admin") + messages.getString("friend-admin-not-friends").replace("{player1}", args[2]).replace("{player2}", args[3])
            )
         );
      }
   }

   public static void executeReload(Invocation invocation) {
      Player p = (Player)invocation.source();
      p.sendMessage(MessageManager.convert(messages.getString("prefix-admin") + messages.getString("friend-admin-reloading")));
      if (Friends.reload()) {
         MessageManager.log(messages.getString("log-admin-reload").replace("{admin}", p.getUsername()));
         p.sendMessage(MessageManager.convert(messages.getString("prefix-admin") + messages.getString("friend-admin-reload-successful")));
      }
   }
}
