package me.dawey.friends.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import java.sql.SQLException;
import me.dawey.friends.Friends;
import me.dawey.friends.database.entities.FriendPlayer;

public class PlayerDatabase {

   private static FriendPlayer getPlayer(String pName) throws SQLException {
       Dao<FriendPlayer, Integer> dao = Friends.database.getFriendPlayerDao();
       QueryBuilder<FriendPlayer, Integer> qb = dao.queryBuilder();
       qb.where().eq("player_name", pName);
       return dao.queryForFirst(qb.prepare());
   }

   public static String getPlayerName(int id) {
       try {
           Dao<FriendPlayer, Integer> dao = Friends.database.getFriendPlayerDao();
           FriendPlayer fp = dao.queryForId(id);
           if (fp != null) {
               return fp.getPlayerName();
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return null;
   }

   public static int getPlayerId(String pName) {
       try {
           FriendPlayer fp = getPlayer(pName);
           if (fp != null) {
               return fp.getId();
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return -1;
   }

   public static long getLastOnline(String pName) {
      try {
         FriendPlayer fp = getPlayer(pName);
         if (fp != null) {
             return fp.getLastOnline(); // Make sure FriendPlayer has this getter
         }
      } catch (SQLException var6) {
         var6.printStackTrace();
      }
      return 0L;
   }

   public static void toggleRequest(String pName) {
      try {
         Dao<FriendPlayer, Integer> dao = Friends.database.getFriendPlayerDao();
         FriendPlayer fp = getPlayer(pName);
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
         FriendPlayer fp = getPlayer(pName);
         if (fp != null) {
             return fp.isRequestEnabled();
         }
      } catch (SQLException var4) {
         var4.printStackTrace();
         return false;
      }
      return false;
   }

   public static void createPlayer(String pName, String uuid) {
      try {
         Dao<FriendPlayer, Integer> dao = Friends.database.getFriendPlayerDao();
         FriendPlayer fp = getPlayer(pName);
         
         if (fp == null) {
             // Check via UUID too maybe?
             QueryBuilder<FriendPlayer, Integer> qb = dao.queryBuilder();
             qb.where().eq("player_uuid", uuid);
             fp = dao.queryForFirst(qb.prepare());
         }

         if (fp == null) {
             fp = new FriendPlayer(uuid, pName, System.currentTimeMillis(), true);
             dao.create(fp);
         } else {
             // Update name if changed
             if (!fp.getPlayerName().equals(pName)) {
                 fp.setPlayerName(pName);
                 dao.update(fp);
             }
         }
      } catch (SQLException var3) {
         var3.printStackTrace();
      }
   }

   public static void setLastOnline(String pName) {
      try {
         Dao<FriendPlayer, Integer> dao = Friends.database.getFriendPlayerDao();
         FriendPlayer fp = getPlayer(pName);
         
         if (fp != null) {
             fp.setLastOnline(System.currentTimeMillis());
             dao.update(fp);
         }

      } catch (SQLException var3) {
         var3.printStackTrace();
      }
   }
}
