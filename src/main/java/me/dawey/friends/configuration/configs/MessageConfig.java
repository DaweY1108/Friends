package me.dawey.friends.configuration.configs;

import java.io.File;
import me.dawey.friends.configuration.Configuration;

public class MessageConfig extends Configuration {
   public MessageConfig(File folder, String fileName) {
      super(folder, fileName, "/messages/" + fileName);
      this.load();
   }
}
