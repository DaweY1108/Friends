package me.dawey.friends.commands;

import com.velocitypowered.api.command.SimpleCommand.Invocation;
import com.velocitypowered.api.proxy.Player;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.database.FriendDatabase;
import me.dawey.friends.utils.MessageManager;

public class FriendRemoveCommand {
   private static MessageConfig messages = ConfigManager.getMessages();

   public static void execute(Invocation invocation) {
      Player p = (Player)invocation.source();
      String[] args = (String[])invocation.arguments();
      if (!p.getUsername().equalsIgnoreCase(args[1])) {
         if (FriendDatabase.areFriends(p.getUsername(), args[1])) {
            FriendDatabase.deleteFriend(p.getUsername(), args[1]);
            p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-removed").replace("{player}", args[1])));
            MessageManager.log(messages.getString("log-remove").replace("{player1}", p.getUsername()).replace("{player2}", args[1]));
         } else {
            p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("not-friends").replace("{player}", args[1])));
         }
      } else {
         p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-remove-yourself")));
      }
   }
}
