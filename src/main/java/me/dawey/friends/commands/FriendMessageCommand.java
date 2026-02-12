package me.dawey.friends.commands;

import com.velocitypowered.api.command.SimpleCommand.Invocation;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.dawey.friends.Friends;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.database.FriendDatabase;
import me.dawey.friends.utils.MessageManager;

public class FriendMessageCommand {
   private static ProxyServer proxy = Friends.getProxyServer();
   private static MessageConfig messages = ConfigManager.getMessages();

   public static void execute(Invocation invocation) {
      Player p = (Player)invocation.source();
      String[] args = (String[])invocation.arguments();
      if (!p.getUsername().equalsIgnoreCase(args[1])) {
         if (FriendDatabase.areFriends(p.getUsername(), args[1])) {
            Optional<Player> temp = proxy.getPlayer(args[1]);
            if (!temp.isEmpty()) {
               Player f = temp.get();
               List<String> argList = new ArrayList<>();
               argList.clear();

               for (int i = 2; i < args.length; i++) {
                  argList.add(args[i]);
               }

               String message = String.join(" ", argList);
               if (!ConfigManager.getpluginConfig().enableColoredMessage()) {
                  message = MessageManager.clearColorCodes(message);
               }

               p.sendMessage(
                  MessageManager.convert(messages.getString("friend-message-sender").replace("{player}", f.getUsername()).replace("{message}", message))
               );
               f.sendMessage(
                  MessageManager.convert(messages.getString("friend-message-reciever").replace("{player}", p.getUsername()).replace("{message}", message))
               );
               MessageManager.log(
                  messages.getString("log-message")
                     .replace("{player1}", p.getUsername())
                     .replace("{player2}", f.getUsername())
                     .replace("{message}", MessageManager.clearColorCodes(message))
               );
            } else {
               p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("player-offline").replace("{player}", args[1])));
            }
         } else {
            p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("not-friends").replace("{player}", args[1])));
         }
      } else {
         p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-message-yourself")));
      }
   }
}
