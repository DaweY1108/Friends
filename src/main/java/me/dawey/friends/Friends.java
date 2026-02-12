package me.dawey.friends;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import java.nio.file.Path;
import java.sql.SQLException;
import me.dawey.friends.commands.FriendAddCommand;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.database.SQL;
import me.dawey.friends.events.PlayerConnection;
import me.dawey.friends.utils.MessageManager;
import me.dawey.friends.utils.ServerLocale;
import org.slf4j.Logger;

@Plugin(id = "friends", name = "Friends", version = "1.0", description = "A Velocity Friends plugin", authors = "DaweY")
public class Friends {
   private static ProxyServer server;
   private static Logger logger;
   private static MessageConfig messages;
   public static SQL database;
   public static ServerLocale locale;

   @Inject
   public Friends(ProxyServer server, Logger logger, @DataDirectory Path folder) {
      Friends.server = server;
      Friends.logger = logger;
      new ConfigManager(folder.toFile());
   }

   @Subscribe
   public void onProxyInitialization(ProxyInitializeEvent event) throws SQLException {
      MessageManager.logHeader();
      locale = new ServerLocale();
      database = new SQL();
      messages = ConfigManager.getMessages();
      FriendAddCommand.runCooldownCount();
      server.getEventManager().register(this, new PlayerConnection(server));
      this.registerCommands();
   }

   public void registerCommands() {
      CommandManager cManager = getProxyServer().getCommandManager();

      for (String command : messages.getStringList("command-aliases")) {
         cManager.register(cManager.metaBuilder(command).build(), new FriendsCommand(server));
      }
   }

   public static String getLocale() {
      return locale.getLocale();
   }

   public static ProxyServer getProxyServer() {
      return server;
   }

   public static Logger getLogger() {
      return logger;
   }

   public static boolean reload() {
      ConfigManager.reloadAll();
      locale = new ServerLocale();
      if (database != null) {
          database.close();
      }
      database = new SQL();

      messages = ConfigManager.getMessages();
      return true;
   }
}
