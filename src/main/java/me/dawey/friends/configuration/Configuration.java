package me.dawey.friends.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import me.dawey.friends.Friends;
import org.yaml.snakeyaml.Yaml;

public class Configuration implements ConfigurationSelection {
   private final File file;
   private Map<String, Object> data;
   protected String resourcePath;

   public Configuration(File folder, String fileName) {
      this(folder, fileName, "/" + fileName);
   }

   public Configuration(File folder, String fileName, String resourcePath) {
      this.file = new File(folder, fileName);
      this.resourcePath = resourcePath;
      if (!this.file.getParentFile().exists()) {
         this.file.getParentFile().mkdirs();
      }
   }

   public void load() {
      if (!this.file.exists()) {
         try (InputStream input = Friends.class.getResourceAsStream(this.resourcePath)) {
            if (input != null) {
               Files.copy(input, this.file.toPath());
            } else {
               this.file.createNewFile();
            }
         } catch (IOException var9) {
            var9.printStackTrace();
            return;
         }
      }

      try (InputStream inputStream = new FileInputStream(this.file)) {
         Yaml yaml = new Yaml();
         this.data = (Map<String, Object>)yaml.load(inputStream);
         if (this.data == null) {
            this.data = Collections.emptyMap();
         }
      } catch (IOException var7) {
         var7.printStackTrace();
      }
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

   private String getChild(String path) {
      int index = path.indexOf(46);
      return index == -1 ? path : path.substring(index + 1);
   }

   public <T> T get(String path, T def) {
      ConfigurationSection section = this.getSection(path);
      Object val;
      if (section == this.getSection("path")) {
         val = this.data.get(path);
      } else {
         val = section.get(this.getChild(path));
      }

      if (val == null && def instanceof Configuration) {
         this.data.put(path, def);
      }

      return (T)(val != null ? val : def);
   }

   @Override
   public String getString(String path) {
      return this.data.get(path) instanceof String ? (String)this.data.get(path) : null;
   }

   public List<?> getList(String path) {
      Object def = this.data.get(path);
      return this.getList(path, def instanceof List ? (List)def : Collections.EMPTY_LIST);
   }

   public List<?> getList(String path, List<?> def) {
      Object val = this.get(path, def);
      return val instanceof List ? (List)val : def;
   }

   public List<String> getStringList(String path) {
      List<?> list = this.getList(path);
      List<String> result = new ArrayList<>();

      for (Object object : list) {
         if (object instanceof String) {
            result.add((String)object);
         }
      }

      return result;
   }

   @Override
   public int getInt(String path) {
      return this.data.get(path) instanceof Integer ? (Integer)this.data.get(path) : 0;
   }

   @Override
   public ArrayList<Integer> getIntArray(String path) {
      return this.data.get(path) instanceof ArrayList ? (ArrayList)this.data.get(path) : null;
   }
}
