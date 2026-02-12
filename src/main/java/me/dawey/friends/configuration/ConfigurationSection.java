package me.dawey.friends.configuration;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

public class ConfigurationSection implements ConfigurationSelection {
   private Map<String, Object> data;
   private String key;

   public ConfigurationSection(Map<String, Object> data) {
      this.data = data;
   }

   public ConfigurationSection(Map<String, Object> data, String key) {
      this.data = data;
      this.key = key;
   }

   @Override
   public void set(String path, Object value) {
      this.data.put(path, value);
   }

   @Override
   public Object get(String path) {
      return this.data.get(path);
   }

   @Override
   public ConfigurationSection getSection(String path) {
      return this.data.get(path) instanceof Map ? new ConfigurationSection((Map<String, Object>)this.data.get(path)) : null;
   }

   @Override
   public ArrayList<ConfigurationSection> getAllSections(String path) {
      Map<String, Object> temp = (Map<String, Object>)this.get(path);
      ArrayList<ConfigurationSection> sections = new ArrayList<>();

      for (Entry<String, Object> section : temp.entrySet()) {
         sections.add(new ConfigurationSection((Map<String, Object>)section.getValue(), section.getKey()));
      }

      return sections;
   }

   @Override
   public String getString(String path) {
      return this.data.get(path) instanceof String ? (String)this.data.get(path) : null;
   }

   @Override
   public int getInt(String path) {
      return this.data.get(path) instanceof Integer ? (Integer)this.data.get(path) : 0;
   }

   @Override
   public ArrayList<Integer> getIntArray(String path) {
      return this.data.get(path) instanceof ArrayList ? (ArrayList)this.data.get(path) : null;
   }

   public String getKey() {
      return this.key;
   }

   public ArrayList<String> getKeys() {
      return new ArrayList<>(this.data.keySet());
   }
}
