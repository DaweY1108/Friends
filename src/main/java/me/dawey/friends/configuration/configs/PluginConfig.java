package me.dawey.friends.configuration.configs;

import java.io.File;
import me.dawey.friends.configuration.Configuration;
import me.dawey.friends.configuration.ConfigurationSection;

public class PluginConfig extends Configuration {
   ConfigurationSection section;

   public PluginConfig(File folder) {
      super(folder, "config.yml");
      this.load();
      this.section = this.getSection("MYSQL");
   }

   public boolean MYSQLenabled() {
      return (Boolean)this.section.get("enabled");
   }

   public String MYSQLhostname() {
      return this.section.getString("hostname");
   }

   public String MYSQLdatabase() {
      return this.section.getString("database");
   }

   public String MYSQLusername() {
      return this.section.getString("username");
   }

   public String MYSQLpassword() {
      return this.section.getString("password");
   }

   public int MYSQLport() {
      return (Integer)this.section.get("port");
   }

   public boolean MYSQLuseSSL() {
      return (Boolean)this.section.get("useSSL");
   }

   public boolean autoLocaleDetect() {
      return (Boolean)this.get("auto-locale-detect");
   }

   public String locale() {
      return this.getString("locale");
   }

   public int requestCooldown() {
      return this.getInt("request-cooldown");
   }

   public boolean enableColoredMessage() {
      return (Boolean)this.get("enable-colored-messages");
   }
}
