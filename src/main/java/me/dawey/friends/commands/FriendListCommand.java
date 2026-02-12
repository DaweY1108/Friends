package me.dawey.friends.commands;

import com.velocitypowered.api.command.SimpleCommand.Invocation;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import java.util.Optional;
import me.dawey.friends.Friends;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.database.FriendDatabase;
import me.dawey.friends.utils.MessageManager;

public class FriendListCommand {
   private static ProxyServer proxy = Friends.getProxyServer();
   private static MessageConfig messages = ConfigManager.getMessages();

   public static void execute(Invocation invocation) {
      Player p = (Player)invocation.source();
      String[] args = (String[])invocation.arguments();
      int page = 1;
      if (args.length == 2) {
         try {
            page = Integer.parseInt(args[1]);
         } catch (Exception var12) {
            page = pageCount(p.getUsername());
         }
      }

      if (page > pageCount(p.getUsername())) {
         page = pageCount(p.getUsername());
      }

      if (page <= 0) {
         page = 1;
      }

      int start = page * 8 - 8;
      int end = page * 8 - 1;
      p.sendMessage(
         MessageManager.convert(
            messages.getString("friend-list-header").replace("{current}", String.valueOf(page)).replace("{max}", String.valueOf(pageCount(p.getUsername())))
         )
      );
      boolean haveFriend = false;
      int currentPlayer = 0;

      for (String s : FriendDatabase.getFriends(p.getUsername())) {
         haveFriend = true;
         if (currentPlayer >= start && currentPlayer <= end) {
            Optional<Player> temp = proxy.getPlayer(s);
            if (temp.isEmpty()) {
               p.sendMessage(
                  MessageManager.convert(messages.getString("friend-list").replace("{friend}", s).replace("{state}", messages.getString("friend-list-offline")))
               );
            } else {
               Player f = temp.get();
               p.sendMessage(
                  MessageManager.convert(
                     messages.getString("friend-list")
                        .replace("{friend}", s)
                        .replace(
                           "{state}",
                           messages.getString("friend-list-online")
                              .replace("{server}", MessageManager.getAlias(((ServerConnection)f.getCurrentServer().get()).getServerInfo().getName()))
                        )
                  )
               );
            }
         }

         currentPlayer++;
      }

      if (!haveFriend) {
         p.sendMessage(MessageManager.convert(messages.getString("no-friends")));
      }

      p.sendMessage(MessageManager.convert(messages.getString("friend-list-footer")));
      MessageManager.log(messages.getString("log-list").replace("{player}", p.getUsername()));
   }

   private static int pageCount(String pName) {
      int i = 0;

      for (String s : FriendDatabase.getFriends(pName)) {
         i++;
      }

      int pageCount = i / 8;
      if (i % 8 > 0 || i == 0) {
         pageCount++;
      }

      return pageCount;
   }
}
