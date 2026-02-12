package me.dawey.friends.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.util.Optional;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.database.FriendDatabase;
import me.dawey.friends.database.PlayerDatabase;
import me.dawey.friends.utils.MessageManager;

public class PlayerConnection {
   private MessageConfig messages = ConfigManager.getMessages();
   ProxyServer proxy;

   public PlayerConnection(ProxyServer proxy) {
      this.proxy = proxy;
   }

   @Subscribe
   public void onPostLogin(PostLoginEvent e) {
      Player p = e.getPlayer();
      PlayerDatabase.createPlayer(p.getUsername(), p.getUniqueId().toString());
      PlayerDatabase.setLastOnline(p.getUsername());

      for (String s : FriendDatabase.getFriends(p.getUsername())) {
         for (Player online : this.proxy.getAllPlayers()) {
            Optional<Player> temp = this.proxy.getPlayer(s);
            if (!temp.isEmpty()) {
               Player friend = temp.get();
               if (online == friend && friend.isActive()) {
                  friend.sendMessage(
                     MessageManager.convert(
                        this.messages.getString("prefix") + this.messages.getString("friend-connected").replace("{friend}", p.getUsername())
                     )
                  );
               }
            }
         }
      }
   }

   @Subscribe
   public void onDisconnect(DisconnectEvent e) {
      Player p = e.getPlayer();
      PlayerDatabase.setLastOnline(p.getUsername());
      PlayerDatabase.createPlayer(p.getUsername(), p.getUniqueId().toString());

      for (String s : FriendDatabase.getFriends(p.getUsername())) {
         for (Player online : this.proxy.getAllPlayers()) {
            Optional<Player> temp = this.proxy.getPlayer(s);
            if (!temp.isEmpty()) {
               Player friend = temp.get();
               if (online == friend && friend.isActive()) {
                  friend.sendMessage(
                     MessageManager.convert(
                        this.messages.getString("prefix") + this.messages.getString("friend-disconnected").replace("{friend}", p.getUsername())
                     )
                  );
               }
            }
         }
      }
   }

   @Subscribe
   public void onServerChange(ServerConnectedEvent e) {
      Player p = e.getPlayer();

      for (String s : FriendDatabase.getFriends(p.getUsername())) {
         for (Player online : this.proxy.getAllPlayers()) {
            Optional<Player> temp = this.proxy.getPlayer(s);
            if (!temp.isEmpty()) {
               Player friend = temp.get();
               if (!e.getPreviousServer().isEmpty() && online == friend && friend.isActive()) {
                  friend.sendMessage(
                     MessageManager.convert(
                        this.messages.getString("prefix")
                           + this.messages
                              .getString("friend-server-change")
                              .replace("{friend}", p.getUsername())
                              .replace("{from}", MessageManager.getAlias(((RegisteredServer)e.getPreviousServer().get()).getServerInfo().getName()))
                              .replace("{to}", MessageManager.getAlias(e.getServer().getServerInfo().getName()))
                     )
                  );
               }
            }
         }
      }
   }
}
