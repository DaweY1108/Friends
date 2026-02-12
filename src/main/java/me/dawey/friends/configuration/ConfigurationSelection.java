package me.dawey.friends.configuration;

import java.util.ArrayList;

public interface ConfigurationSelection {
   void set(String var1, Object var2);

   Object get(String var1);

   ConfigurationSection getSection(String var1);

   ArrayList<ConfigurationSection> getAllSections(String var1);

   String getString(String var1);

   int getInt(String var1);

   ArrayList<Integer> getIntArray(String var1);
}
