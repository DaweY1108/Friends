package me.dawey.friends.utils;

import java.util.Locale;
import me.dawey.friends.configuration.ConfigManager;

public class ServerLocale {
   public String locale = "en_EN";

   public ServerLocale() {
      this.setLocale();
      MessageManager.log("Using locale: " + this.getLocale());
   }

   public void setLocale() {
      if (ConfigManager.getpluginConfig().autoLocaleDetect()) {
         String sysLocale = Locale.getDefault().toString();
         if (ConfigManager.isLocaleAvailable(sysLocale)) {
            this.locale = sysLocale;
         } else if (ConfigManager.isLocaleAvailable("en_EN")) {
            this.locale = "en_EN";
         }
      } else {
         String temp = ConfigManager.getpluginConfig().locale();
         if (ConfigManager.isLocaleAvailable(temp)) {
            this.locale = temp;
         } else {
            MessageManager.log(temp + " locale not found! Available locales: " + ConfigManager.getAvailableLocales());
            if (ConfigManager.isLocaleAvailable("en_EN")) {
                this.locale = "en_EN";
            }
         }
      }
   }

   public void setLocale(String locale) {
      if (ConfigManager.isLocaleAvailable(locale)) {
          this.locale = locale;
      }
   }

   public String getLocale() {
      return this.locale;
   }
}
