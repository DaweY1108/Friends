package me.dawey.friends;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.SimpleCommand.Invocation;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.dawey.friends.commands.FriendAddCommand;
import me.dawey.friends.commands.FriendAdminCommand;
import me.dawey.friends.commands.FriendInfoCommand;
import me.dawey.friends.commands.FriendListCommand;
import me.dawey.friends.commands.FriendMessageCommand;
import me.dawey.friends.commands.FriendRemoveCommand;
import me.dawey.friends.commands.FriendToggleCommand;
import me.dawey.friends.commands.FriendTpCommand;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.database.FriendDatabase;
import me.dawey.friends.utils.MessageManager;

public class FriendsCommand implements SimpleCommand {
   private ProxyServer proxy;
   private MessageConfig messages = ConfigManager.getMessages();

   public FriendsCommand(ProxyServer proxy) {
      this.proxy = proxy;
   }

   public void execute(Invocation invocation) {
      String[] args = (String[])invocation.arguments();
      if (invocation.source() instanceof Player) {
         Player p = (Player)invocation.source();
         if (p.hasPermission("friends.player")) {
            if (args.length > 0) {
               for (String s : this.messages.getStringList("command-list")) {
                  if (args[0].equalsIgnoreCase(s)) {
                     if (args.length != 1 && args.length != 2) {
                        p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-list-usage")));
                     } else {
                        FriendListCommand.execute(invocation);
                     }

                     return;
                  }
               }

               for (String sx : this.messages.getStringList("command-request")) {
                  if (args[0].equalsIgnoreCase(sx)) {
                     if (args.length == 2) {
                        FriendAddCommand.executeRequest(invocation);
                     } else {
                        p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-request-send-usage")));
                     }

                     return;
                  }
               }

               for (String sxx : this.messages.getStringList("command-accept")) {
                  if (args[0].equalsIgnoreCase(sxx)) {
                     if (args.length == 2) {
                        FriendAddCommand.executeAccept(invocation);
                     } else {
                        p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-request-accept-usage")));
                     }

                     return;
                  }
               }

               for (String sxxx : this.messages.getStringList("command-decline")) {
                  if (args[0].equalsIgnoreCase(sxxx)) {
                     if (args.length == 2) {
                        FriendAddCommand.executeDecline(invocation);
                     } else {
                        p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-request-decline-usage")));
                     }

                     return;
                  }
               }

               for (String sxxxx : this.messages.getStringList("command-remove")) {
                  if (args[0].equalsIgnoreCase(sxxxx)) {
                     if (args.length == 2) {
                        FriendRemoveCommand.execute(invocation);
                     } else {
                        p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-remove-usage")));
                     }

                     return;
                  }
               }

               for (String sxxxxx : this.messages.getStringList("command-message")) {
                  if (args[0].equalsIgnoreCase(sxxxxx)) {
                     if (args.length > 2) {
                        FriendMessageCommand.execute(invocation);
                     } else {
                        p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-message-usage")));
                     }

                     return;
                  }
               }

               for (String sxxxxxx : this.messages.getStringList("command-info")) {
                  if (args[0].equalsIgnoreCase(sxxxxxx)) {
                     if (args.length == 2) {
                        FriendInfoCommand.execute(invocation);
                     } else {
                        p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-info-usage")));
                     }

                     return;
                  }
               }

               for (String sxxxxxxx : this.messages.getStringList("command-tp")) {
                  if (args[0].equalsIgnoreCase(sxxxxxxx)) {
                     if (args.length == 2) {
                        FriendTpCommand.execute(invocation);
                     } else {
                        p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-tp-usage")));
                     }

                     return;
                  }
               }

               for (String sxxxxxxxx : this.messages.getStringList("command-request-toggle")) {
                  if (args[0].equalsIgnoreCase(sxxxxxxxx)) {
                     if (args.length == 1) {
                        FriendToggleCommand.execute(invocation);
                     } else {
                        p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-toggle-usage")));
                     }

                     return;
                  }
               }

               if (p.hasPermission("friends.admin")) {
                  for (String sxxxxxxxxx : this.messages.getStringList("command-admin")) {
                     if (args[0].equalsIgnoreCase(sxxxxxxxxx)) {
                        if (args.length > 1) {
                           for (String s1 : this.messages.getStringList("admin-command-remove")) {
                              if (args[1].equalsIgnoreCase(s1)) {
                                 if (args.length == 4) {
                                    FriendAdminCommand.executeDelete(invocation);
                                    return;
                                 }

                                 p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-admin-remove-usage")));
                                 return;
                              }
                           }

                           for (String s1x : this.messages.getStringList("admin-command-add")) {
                              if (args[1].equalsIgnoreCase(s1x)) {
                                 if (args.length == 4) {
                                    FriendAdminCommand.executeAdd(invocation);
                                    return;
                                 }

                                 p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-admin-add-usage")));
                                 return;
                              }
                           }

                           for (String s1xx : this.messages.getStringList("admin-command-reload")) {
                              if (args[1].equalsIgnoreCase(s1xx)) {
                                 if (args.length == 2) {
                                    FriendAdminCommand.executeReload(invocation);
                                    return;
                                 }

                                 p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("friend-admin-reload-usage")));
                                 return;
                              }
                           }
                        } else {
                           this.sendAdminHelp(p);
                        }
                     }
                  }

                  return;
               }

               p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("no-permission")));
            } else {
               this.sendHelp(p);
            }
         } else {
            p.sendMessage(MessageManager.convert(this.messages.getString("prefix") + this.messages.getString("no-permission")));
         }
      } else {
         MessageManager.log(this.messages.getString("not-player"));
      }
   }

   public List<String> suggest(Invocation invocation) {
      Player p = (Player)invocation.source();
      String[] args = (String[])invocation.arguments();
      if (p.hasPermission("friends.player")) {
         if (args.length == 1) {
            List<String> commands = new ArrayList<>();

            for (String s : this.messages.getStringList("command-request")) {
               commands.add(s);
            }

            for (String s : this.messages.getStringList("command-accept")) {
               commands.add(s);
            }

            for (String s : this.messages.getStringList("command-decline")) {
               commands.add(s);
            }

            for (String s : this.messages.getStringList("command-remove")) {
               commands.add(s);
            }

            for (String s : this.messages.getStringList("command-info")) {
               commands.add(s);
            }

            for (String s : this.messages.getStringList("command-message")) {
               commands.add(s);
            }

            for (String s : this.messages.getStringList("command-tp")) {
               commands.add(s);
            }

            for (String s : this.messages.getStringList("command-list")) {
               commands.add(s);
            }

            for (String s : this.messages.getStringList("command-request-toggle")) {
               commands.add(s);
            }

            if (p.hasPermission("friends.admin")) {
               for (String s : this.messages.getStringList("command-admin")) {
                  commands.add(s);
               }
            }

            Collections.sort(commands);
            return commands;
         }

         if (args.length == 2 && p.hasPermission("friends.admin")) {
            for (String s : this.messages.getStringList("command-admin")) {
               if (args[0].equalsIgnoreCase(s)) {
                  List<String> commands = new ArrayList<>();

                  for (String s1 : this.messages.getStringList("admin-command-add")) {
                     commands.add(s1);
                  }

                  for (String s1 : this.messages.getStringList("admin-command-remove")) {
                     commands.add(s1);
                  }

                  for (String s1 : this.messages.getStringList("admin-command-reload")) {
                     commands.add(s1);
                  }

                  Collections.sort(commands);
                  return commands;
               }
            }
         }

         if (args.length == 2) {
            for (String sx : this.messages.getStringList("command-request")) {
               if (args[0].equalsIgnoreCase(sx)) {
                  return this.players(p);
               }
            }

            for (String sxx : this.messages.getStringList("command-accept")) {
               if (args[0].equalsIgnoreCase(sxx)) {
                  return this.players(p);
               }
            }

            for (String sxxx : this.messages.getStringList("command-decline")) {
               if (args[0].equalsIgnoreCase(sxxx)) {
                  return this.players(p);
               }
            }

            for (String sxxxx : this.messages.getStringList("command-remove")) {
               if (args[0].equalsIgnoreCase(sxxxx)) {
                  return FriendDatabase.getFriends(p.getUsername());
               }
            }

            for (String sxxxxx : this.messages.getStringList("command-info")) {
               if (args[0].equalsIgnoreCase(sxxxxx)) {
                  return FriendDatabase.getFriends(p.getUsername());
               }
            }

            for (String sxxxxxx : this.messages.getStringList("command-message")) {
               if (args[0].equalsIgnoreCase(sxxxxxx)) {
                  return FriendDatabase.getFriends(p.getUsername());
               }
            }

            for (String sxxxxxxx : this.messages.getStringList("command-tp")) {
               if (args[0].equalsIgnoreCase(sxxxxxxx)) {
                  return FriendDatabase.getFriends(p.getUsername());
               }
            }
         }
      }

      return suggest(invocation);
   }

   public void sendHelp(Player p) {
      for (String s : this.messages.getStringList("help")) {
         p.sendMessage(MessageManager.convert(s));
      }
   }

   public void sendAdminHelp(Player p) {
      for (String s : this.messages.getStringList("admin-help")) {
         p.sendMessage(MessageManager.convert(s));
      }
   }

   public List<String> players(Player p) {
      List<String> players = new ArrayList<>();

      for (Player player : this.proxy.getAllPlayers()) {
         players.add(player.getUsername());
      }

      players.remove(p.getUsername());
      return players;
   }
}
