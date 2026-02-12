package me.dawey.friends.database;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import me.dawey.friends.Friends;
import me.dawey.friends.database.entities.FriendPlayer;

public class PlayerDatabase {

   public static long getLastOnline(String pName) {
      try {
         Dao<FriendPlayer, String> dao = Friends.database.getFriendPlayerDao();
         FriendPlayer fp = dao.queryForId(pName);
         if (fp != null) {
             return fp.getLastOnline();
         }
      } catch (SQLException var6) {
         var6.printStackTrace();
      }
      return 0L;
   }

   public static void toggleRequest(String pName) {
      try {
         Dao<FriendPlayer, String> dao = Friends.database.getFriendPlayerDao();
         FriendPlayer fp = dao.queryForId(pName);
         if (fp != null) {
             fp.setRequestEnabled(!fp.isRequestEnabled());
             dao.update(fp);
         }
      } catch (SQLException var3) {
         var3.printStackTrace();
      }
   }

   public static boolean requestEnabled(String pName) {
      try {
         Dao<FriendPlayer, String> dao = Friends.database.getFriendPlayerDao();
         FriendPlayer fp = dao.queryForId(pName);
         if (fp != null) {
             return fp.isRequestEnabled();
         }
      } catch (SQLException var4) {
         var4.printStackTrace();
         return false;
      }
      return false;
   }

   public static void createTable(String pName) {
      try {
         if (getLastOnline(pName) == 0L) {
             Dao<FriendPlayer, String> dao = Friends.database.getFriendPlayerDao();
             if (!dao.idExists(pName)) {
                 FriendPlayer fp = new FriendPlayer(pName, System.currentTimeMillis(), true);
                 dao.create(fp);
             }
         }
      } catch (SQLException var3) {
         var3.printStackTrace();
      }
   }

   public static void setLastOnline(String pName) {
      try {
         Dao<FriendPlayer, String> dao = Friends.database.getFriendPlayerDao();
         FriendPlayer fp = dao.queryForId(pName);
         
         if (fp == null) {
             fp = new FriendPlayer(pName, System.currentTimeMillis(), false);
             dao.create(fp);
         } else {
             fp.setLastOnline(System.currentTimeMillis());
             dao.update(fp);
         }

      } catch (SQLException var3) {
         var3.printStackTrace();
      }
   }
}
