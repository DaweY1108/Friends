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

public class FriendTpCommand {
   private static ProxyServer proxy = Friends.getProxyServer();
   private static MessageConfig messages = ConfigManager.getMessages();

   public static void execute(Invocation invocation) {
      String[] args = (String[])invocation.arguments();
      Player p = (Player)invocation.source();
      if (FriendDatabase.areFriends(p.getUsername(), args[1])) {
         if (!p.getUsername().equalsIgnoreCase(args[1])) {
            Optional<Player> temp = proxy.getPlayer(args[1]);
            if (!temp.isEmpty()) {
               Player f = temp.get();
               if (!((ServerConnection)p.getCurrentServer().get())
                  .getServerInfo()
                  .getName()
                  .equalsIgnoreCase(((ServerConnection)f.getCurrentServer().get()).getServerInfo().getName())) {
                  p.createConnectionRequest(((ServerConnection)f.getCurrentServer().get()).getServer()).connect();
                  p.sendMessage(
                     MessageManager.convert(
                        messages.getString("prefix")
                           + messages.getString("friend-tp-teleporting")
                              .replace("{server}", MessageManager.getAlias(((ServerConnection)f.getCurrentServer().get()).getServerInfo().getName()))
                     )
                  );
                  MessageManager.log(
                     messages.getString("log-tp")
                        .replace("{player1}", p.getUsername())
                        .replace("{player2}", args[1])
                        .replace("{server}", MessageManager.getAlias(((ServerConnection)f.getCurrentServer().get()).getServerInfo().getName()))
                  );
               } else {
                  p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-tp-same-server").replace("{player}", args[1])));
               }
            } else {
               p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("player-offline").replace("{player}", args[1])));
            }
         } else {
            p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-tp-yourself")));
         }
      } else {
         p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("not-friends").replace("{player}", args[1])));
      }
   }
}
