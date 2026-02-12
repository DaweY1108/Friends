package me.dawey.friends.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import me.dawey.friends.Friends;
import me.dawey.friends.database.entities.Friendship;

public class FriendDatabase {

   public static void addFriend(String pname, String fname) {
      try {
         Dao<Friendship, Integer> dao = Friends.database.getFriendshipDao();
         long friendsSince = System.currentTimeMillis();
         
         int pId = PlayerDatabase.getPlayerId(pname);
         int fId = PlayerDatabase.getPlayerId(fname);
         
         if (pId != -1 && fId != -1) {
             Friendship f1 = new Friendship(pId, fId, friendsSince);
             dao.create(f1);
             
             Friendship f2 = new Friendship(fId, pId, friendsSince);
             dao.create(f2);
         }
      } catch (SQLException var8) {
         System.out.println("Error trying to insert to the database!");
         var8.printStackTrace();
      }
   }

   public static long getFriendsSince(String pName, String fName) {
      try {
         Dao<Friendship, Integer> dao = Friends.database.getFriendshipDao();
         int pId = PlayerDatabase.getPlayerId(pName);
         int fId = PlayerDatabase.getPlayerId(fName);
         
         if (pId != -1 && fId != -1) {
             QueryBuilder<Friendship, Integer> qb = dao.queryBuilder();
             qb.where().eq("player_id", pId).and().eq("friend_id", fId);
             Friendship friendship = dao.queryForFirst(qb.prepare());
             
             if (friendship != null) {
                return friendship.getFriendsSince();
             }
         }
      } catch (SQLException var7) {
         var7.printStackTrace();
      }
      return 0L;
   }

   public static boolean areFriends(String pName, String fName) {
      try {
         Dao<Friendship, Integer> dao = Friends.database.getFriendshipDao();
         int pId = PlayerDatabase.getPlayerId(pName);
         int fId = PlayerDatabase.getPlayerId(fName);
         
         if (pId != -1 && fId != -1) {
             QueryBuilder<Friendship, Integer> qb = dao.queryBuilder();
             qb.where().eq("player_id", pId).and().eq("friend_id", fId);
             return qb.countOf() > 0;
         }
      } catch (SQLException var5) {
         var5.printStackTrace();
         return false;
      }
      return false;
   }

   public static List<String> getFriends(String pname) {
      List<String> friendNames = new ArrayList<>();
      try {
         int pId = PlayerDatabase.getPlayerId(pname);
         if (pId != -1) {
             Dao<Friendship, Integer> dao = Friends.database.getFriendshipDao();
             QueryBuilder<Friendship, Integer> qb = dao.queryBuilder();
             qb.where().eq("player_id", pId);
             List<Friendship> friendships = dao.query(qb.prepare());

             for (Friendship f : friendships) {
                String fName = PlayerDatabase.getPlayerName(f.getFriendId());
                if (fName != null) {
                    friendNames.add(fName);
                }
             }
         }
      } catch (SQLException var5) {
         var5.printStackTrace();
      }
      return friendNames;
   }

   public static void deleteFriend(String pname, String fname) {
      try {
         Dao<Friendship, Integer> dao = Friends.database.getFriendshipDao();
         int pId = PlayerDatabase.getPlayerId(pname);
         int fId = PlayerDatabase.getPlayerId(fname);
         
         if (pId != -1 && fId != -1) {
             DeleteBuilder<Friendship, Integer> db1 = dao.deleteBuilder();
             db1.where().eq("player_id", pId).and().eq("friend_id", fId);
             db1.delete();

             DeleteBuilder<Friendship, Integer> db2 = dao.deleteBuilder();
             db2.where().eq("player_id", fId).and().eq("friend_id", pId);
             db2.delete();
         }
      } catch (SQLException var6) {
         var6.printStackTrace();
      }
   }

   public static int friendCount(String pName) {
      try {
         int pId = PlayerDatabase.getPlayerId(pName);
         if (pId != -1) {
             Dao<Friendship, Integer> dao = Friends.database.getFriendshipDao();
             QueryBuilder<Friendship, Integer> qb = dao.queryBuilder();
             qb.where().eq("player_id", pId);
             return (int) qb.countOf();
         }
      } catch (SQLException var2) {
         var2.printStackTrace();
         return 0;
      }
      return 0;
   }
}
