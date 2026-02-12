package me.dawey.friends.utils;

import com.velocitypowered.api.plugin.Plugin;
import me.dawey.friends.Friends;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.ConfigurationSection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MessageManager {
   public static String getAlias(String serverName) {
      ConfigurationSection section = ConfigManager.getpluginConfig().getSection("server-aliases");

      for (String key : section.getKeys()) {
         if (key.equalsIgnoreCase(serverName)) {
            return section.getString(key);
         }
      }

      return serverName;
   }

   public static Component convert(String message) {
      return ((Builder)Component.text().append(LegacyComponentSerializer.legacyAmpersand().deserialize(message))).build();
   }

   public static void log(String message) {
      Friends.getLogger().info(message);
   }

   public static String clearColorCodes(String message) {
      message = message.replaceAll("&#[a-fA-F0-9]{6}", "");
      if (message.contains("&0")) {
         message = message.replace("&0", "");
      }

      if (message.contains("&1")) {
         message = message.replace("&1", "");
      }

      if (message.contains("&2")) {
         message = message.replace("&2", "");
      }

      if (message.contains("&3")) {
         message = message.replace("&3", "");
      }

      if (message.contains("&4")) {
         message = message.replace("&4", "");
      }

      if (message.contains("&5")) {
         message = message.replace("&5", "");
      }

      if (message.contains("&6")) {
         message = message.replace("&6", "");
      }

      if (message.contains("&7")) {
         message = message.replace("&7", "");
      }

      if (message.contains("&8")) {
         message = message.replace("&8", "");
      }

      if (message.contains("&9")) {
         message = message.replace("&9", "");
      }

      if (message.contains("&a")) {
         message = message.replace("&a", "");
      }

      if (message.contains("&b")) {
         message = message.replace("&b", "");
      }

      if (message.contains("&c")) {
         message = message.replace("&c", "");
      }

      if (message.contains("&d")) {
         message = message.replace("&d", "");
      }

      if (message.contains("&e")) {
         message = message.replace("&e", "");
      }

      if (message.contains("&f")) {
         message = message.replace("&f", "");
      }

      if (message.contains("&k")) {
         message = message.replace("&k", "");
      }

      if (message.contains("&l")) {
         message = message.replace("&l", "");
      }

      if (message.contains("&m")) {
         message = message.replace("&m", "");
      }

      if (message.contains("&n")) {
         message = message.replace("&n", "");
      }

      if (message.contains("&o")) {
         message = message.replace("&o", "");
      }

      if (message.contains("&r")) {
         message = message.replace("&r", "");
      }

      return message;
   }

   public static void logHeader() {
      String version = "&9Version: " + ((Plugin)Friends.class.getAnnotation(Plugin.class)).version();
      String message = "&9 ______    _                _         %&9|  ____|  (_)              | |        %&9| |__ _ __ _  ___ _ __   __| |___     %&9|  __| '__| |/ _ \\ '_ \\ / _` / __|  %&9| |  | |  | |  __/ | | | (_| \\__ \\  %&9|_|  |_|  |_|\\___|_| |_|\\__,_|___/  %&9%%%";

      for (String string : message.split("%")) {
         Friends.getProxyServer().getConsoleCommandSource().sendMessage(convert(string));
      }
   }
}
