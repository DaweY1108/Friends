package me.dawey.friends.commands;

import com.velocitypowered.api.command.SimpleCommand.Invocation;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import me.dawey.friends.Friends;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.configuration.configs.PluginConfig;
import me.dawey.friends.database.FriendDatabase;
import me.dawey.friends.database.PlayerDatabase;
import me.dawey.friends.utils.MessageManager;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class FriendAddCommand {
   private static ProxyServer proxy = Friends.getProxyServer();
   private static Optional<PluginContainer> plugin = Friends.getProxyServer().getPluginManager().getPlugin("friends");
   private static MessageConfig messages = ConfigManager.getMessages();
   private static PluginConfig config = ConfigManager.getpluginConfig();
   public static Map<String, Long> cooldown = new HashMap<>();

   public static void executeRequest(Invocation invocation) {
      Player p = (Player)invocation.source();
      String[] args = (String[])invocation.arguments();
      if (!p.getUsername().equalsIgnoreCase(args[1])) {
         try {
            Optional<Player> temp = proxy.getPlayer(args[1]);
            if (!temp.isEmpty()) {
               Player friend = temp.get();
               if (!FriendDatabase.areFriends(p.getUsername(), args[1])) {
                  if (PlayerDatabase.requestEnabled(args[1])) {
                     if (!cooldown.containsKey(p.getUsername() + "_" + args[1])) {
                        if (!cooldown.containsKey(args[1] + "_" + p.getUsername())) {
                           if (getMaxFriend(p.getUsername()) <= FriendDatabase.friendCount(p.getUsername()) && !p.hasPermission("friends.admin")) {
                              p.sendMessage(
                                 MessageManager.convert(
                                    messages.getString("prefix")
                                       + messages.getString("friend-request-limit-reached").replace("{max}", String.valueOf(getMaxFriend(p.getUsername())))
                                 )
                              );
                           } else if (getMaxFriend(args[1]) <= FriendDatabase.friendCount(args[1]) && !friend.hasPermission("friends.admin")) {
                              p.sendMessage(
                                 MessageManager.convert(
                                    messages.getString("prefix") + messages.getString("friend-request-limit-reached-other").replace("{player}", args[1])
                                 )
                              );
                           } else {
                              cooldown.put(p.getUsername() + "_" + args[1], (long)config.requestCooldown());
                              friend.sendMessage(
                                 MessageManager.convert(
                                    messages.getString("friend-request-recieved")
                                       .replace("{player}", p.getUsername())
                                       .replace("{cooldown}", String.valueOf(config.requestCooldown()))
                                 )
                              );
                              p.sendMessage(
                                 MessageManager.convert(messages.getString("prefix") + messages.getString("friend-request-sent").replace("{player}", args[1]))
                              );
                              MessageManager.log(messages.getString("log-request-sent").replace("{player1}", p.getUsername()).replace("{player2}", args[1]));
                           }
                        } else {
                           p.sendMessage(
                              MessageManager.convert(
                                 messages.getString("prefix") + messages.getString("friend-request-already-recieved").replace("{player}", args[1])
                              )
                           );
                        }
                     } else {
                        p.sendMessage(
                           MessageManager.convert(messages.getString("prefix") + messages.getString("friend-request-already-sent").replace("{player}", args[1]))
                        );
                     }
                  } else {
                     p.sendMessage(
                        MessageManager.convert(messages.getString("prefix") + messages.getString("friend-request-disabled").replace("{player}", args[1]))
                     );
                  }
               } else {
                  p.sendMessage(
                     MessageManager.convert(messages.getString("prefix") + messages.getString("friend-request-already-friends").replace("{player}", args[1]))
                  );
               }
            } else {
               p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("player-offline").replace("{player}", args[1])));
            }
         } catch (Exception var5) {
         }
      } else {
         p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-request-yourself-send")));
      }
   }

   public static void executeAccept(Invocation invocation) {
      Player p = (Player)invocation.source();
      String[] args = (String[])invocation.arguments();
      if (!p.getUsername().equalsIgnoreCase(args[1])) {
         if (cooldown.containsKey(args[1] + "_" + p.getUsername())) {
            if (getMaxFriend(p.getUsername()) <= FriendDatabase.friendCount(p.getUsername()) && !p.hasPermission("friends.admin")) {
               p.sendMessage(
                  MessageManager.convert(
                     messages.getString("prefix")
                        + messages.getString("friend-request-limit-reached").replace("{max}", String.valueOf(getMaxFriend(p.getUsername())))
                  )
               );
            } else {
               try {
                  Optional<Player> temp = proxy.getPlayer(args[1]);
                  Player friend = temp.get();
                  if (friend.isActive()) {
                     friend.sendMessage(
                        MessageManager.convert(
                           messages.getString("prefix") + messages.getString("friend-request-accepted-recieve").replace("{player}", p.getUsername())
                        )
                     );
                  }

                  p.sendMessage(
                     MessageManager.convert(messages.getString("prefix") + messages.getString("friend-request-accepted-sender").replace("{player}", args[1]))
                  );
                  MessageManager.log(messages.getString("log-request-accepted").replace("{player1}", p.getUsername()).replace("{player2}", args[1]));
                  FriendDatabase.addFriend(p.getUsername(), args[1]);
                  cooldown.remove(args[1] + "_" + p.getUsername());
               } catch (Exception var5) {
               }
            }
         } else {
            p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-request-expired").replace("{player}", args[1])));
         }
      } else {
         p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-request-yourself-action")));
      }
   }

   public static void executeDecline(Invocation invocation) {
      Player p = (Player)invocation.source();
      String[] args = (String[])invocation.arguments();
      if (!p.getUsername().equalsIgnoreCase(args[1])) {
         if (cooldown.containsKey(args[1] + "_" + p.getUsername())) {
            try {
               Optional<Player> temp = proxy.getPlayer(args[1]);
               Player friend = temp.get();
               if (friend.isActive()) {
                  friend.sendMessage(
                     MessageManager.convert(
                        messages.getString("prefix") + messages.getString("friend-request-declined-recieve").replace("{player}", p.getUsername())
                     )
                  );
               }

               p.sendMessage(
                  MessageManager.convert(messages.getString("prefix") + messages.getString("friend-request-declined-sender").replace("{player}", args[1]))
               );
               MessageManager.log(messages.getString("log-request-declined").replace("{player1}", p.getUsername()).replace("{player2}", args[1]));
               cooldown.remove(args[1] + "_" + p.getUsername());
            } catch (Exception var5) {
            }
         } else {
            p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-request-expired").replace("{player}", args[1])));
         }
      } else {
         p.sendMessage(MessageManager.convert(messages.getString("prefix") + messages.getString("friend-request-yourself-action")));
      }
   }

   public static void runCooldownCount() {
      proxy.getScheduler()
         .buildTask(
            plugin.get(),
            () -> {
               for (String key : cooldown.keySet()) {
                  long remaining = cooldown.get(key);
                  if (remaining >= 0L) {
                     cooldown.replace(key, --remaining);
                  }

                  if (remaining <= 0L) {
                     String[] players = key.split("_");
                     String expiredPlayer = players[0];
                     Optional<Player> temp = proxy.getPlayer(expiredPlayer);
                     if (!temp.isEmpty()) {
                        Player p = temp.get();
                        p.sendMessage(
                           MessageManager.convert(
                              messages.getString("prefix") + messages.getString("friend-request-no-reaction").replace("{player}", players[1])
                           )
                        );
                     }

                     cooldown.remove(key);
                  }
               }
            }
         )
         .repeat(1L, TimeUnit.SECONDS)
         .schedule();
   }

   public static int getMaxFriend(String pName) {
      int maxFriend = 0;
      boolean foundPlayer = false;
      User user = LuckPermsProvider.get().getUserManager().getUser(pName);
      Group group = LuckPermsProvider.get().getGroupManager().getGroup(user.getPrimaryGroup());

      for (Node node : user.getNodes()) {
         String perm = node.getKey();
         if (perm.contains("friends.limit.")) {
            String maxInt = perm.replace("friends.limit.", "");
            maxFriend = Integer.parseInt(maxInt);
            foundPlayer = true;
         }
      }

      if (!foundPlayer) {
         for (Node nodex : group.getNodes()) {
            String perm = nodex.getKey();
            if (perm.contains("friends.limit.")) {
               String maxInt = perm.replace("friends.limit.", "");
               maxFriend = Integer.parseInt(maxInt);
            }
         }
      }

      return maxFriend;
   }
}
