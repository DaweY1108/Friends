package me.dawey.friends.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import java.sql.SQLException;
import me.dawey.friends.configuration.ConfigManager;
import me.dawey.friends.configuration.configs.MessageConfig;
import me.dawey.friends.database.entities.FriendPlayer;
import me.dawey.friends.database.entities.Friendship;
import me.dawey.friends.utils.MessageManager;

public class SQL {
   public String host;
   public String database;
   public String username;
   public String password;
   public int port;
   public boolean useSSL;
   private MessageConfig messages = ConfigManager.getMessages();

   private ConnectionSource connectionSource;
   private Dao<Friendship, Integer> friendshipDao;
   private Dao<FriendPlayer, String> friendPlayerDao;

   public SQL() {
      Logger.setGlobalLogLevel(Level.ERROR);
      try {
          if (ConfigManager.getpluginConfig().MYSQLenabled()) {
             MessageManager.log(this.messages.getString("mysql-connecting").replace("{database}", ConfigManager.getpluginConfig().MYSQLdatabase()));
    
             this.host = ConfigManager.getpluginConfig().MYSQLhostname();
             this.database = ConfigManager.getpluginConfig().MYSQLdatabase();
             this.username = ConfigManager.getpluginConfig().MYSQLusername();
             this.password = ConfigManager.getpluginConfig().MYSQLpassword();
             this.port = ConfigManager.getpluginConfig().MYSQLport();
             this.useSSL = ConfigManager.getpluginConfig().MYSQLuseSSL();
             
             // Setup connection string for MySQL
             String databaseUrl = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true&useSSL=" + this.useSSL;
             connectionSource = new JdbcConnectionSource(databaseUrl, this.username, this.password);
             
             MessageManager.log(this.messages.getString("mysql-connected"));
          } else {
             MessageManager.log(this.messages.getString("sqlite-connecting"));
             
             // Setup connection string for SQLite
             String databaseUrl = "jdbc:sqlite:plugins/friends/database.db";
             connectionSource = new JdbcConnectionSource(databaseUrl);
             
             MessageManager.log(this.messages.getString("sqlite-connected"));
          }

          // Initialize DAOs
          friendshipDao = DaoManager.createDao(connectionSource, Friendship.class);
          friendPlayerDao = DaoManager.createDao(connectionSource, FriendPlayer.class);
          
          // Create tables if they don't exist
          if (!friendshipDao.isTableExists()) {
              TableUtils.createTableIfNotExists(connectionSource, Friendship.class);
          }
          if (!friendPlayerDao.isTableExists()) {
              TableUtils.createTableIfNotExists(connectionSource, FriendPlayer.class);
          }

      } catch (SQLException e) {
          if (ConfigManager.getpluginConfig().MYSQLenabled()) {
              MessageManager.log(this.messages.getString("mysql-error"));
          } else {
              MessageManager.log(this.messages.getString("sqlite-error"));
          }
          e.printStackTrace();
      }
   }

   public ConnectionSource getConnectionSource() {
       return connectionSource;
   }

   public Dao<Friendship, Integer> getFriendshipDao() {
       return friendshipDao;
   }

   public Dao<FriendPlayer, String> getFriendPlayerDao() {
       return friendPlayerDao;
   }
   
   public void close() {
       if (connectionSource != null) {
           try {
               connectionSource.close();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
}
