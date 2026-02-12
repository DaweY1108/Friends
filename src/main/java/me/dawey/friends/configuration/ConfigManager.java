package me.dawey.friends.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.dawey.friends.Friends;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.configuration.configs.PluginConfig;

public class ConfigManager {
   private static PluginConfig pluginConfig;
   private static Map<String, MessageConfig> messageConfigMap = new HashMap<>();
   private static File pluginFolder;

   public ConfigManager(File folder) {
      pluginFolder = folder;
      pluginConfig = new PluginConfig(folder);
      
      File messagesFolder = new File(folder, "messages");
      if (!messagesFolder.exists()) {
         messagesFolder.mkdirs();
      }
      new MessageConfig(messagesFolder, "messages_en.yml");
      new MessageConfig(messagesFolder, "messages_hu.yml");

      loadMessages(messagesFolder);
   }

   private static void loadMessages(File messagesFolder) {
      messageConfigMap.clear();
      File[] files = messagesFolder.listFiles();
      
      if (files != null) {
         for (File file : files) {
            String name = file.getName();
            if (name.startsWith("messages_") && name.endsWith(".yml")) {
               String localeName;
               if (name.equals("messages_en.yml")) {
                  localeName = "en_EN";
               } else if (name.equals("messages_hu.yml")) {
                  localeName = "hu_HU";
               } else {
                  localeName = name.substring(9, name.length() - 4); 
               }
               
               messageConfigMap.put(localeName, new MessageConfig(messagesFolder, name));
            }
         }
      }
      if (messageConfigMap.isEmpty()) {
           messageConfigMap.put("en_EN", new MessageConfig(messagesFolder, "messages_en.yml"));
      }
   }

   public static void reloadAll() {
      pluginConfig.load();
      if (pluginFolder != null) {
         loadMessages(new File(pluginFolder, "messages"));
      }
   }

   public static MessageConfig getMessages() {
      MessageConfig config = messageConfigMap.get(Friends.getLocale());
      // Fallback to en_EN if the specific locale is missing but somehow requested
      if (config == null) {
          config = messageConfigMap.get("en_EN");
      }
      return config;
   }

   public static PluginConfig getpluginConfig() {
      return pluginConfig;
   }

   public static boolean isLocaleAvailable(String locale) {
      return messageConfigMap.containsKey(locale);
   }

   public static List<String> getAvailableLocales() {
      return new ArrayList<>(messageConfigMap.keySet());
   }
}
