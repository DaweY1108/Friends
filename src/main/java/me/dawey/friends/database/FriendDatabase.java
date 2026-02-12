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
         
         Friendship f1 = new Friendship(pname, fname, friendsSince);
         dao.create(f1);
         
         Friendship f2 = new Friendship(fname, pname, friendsSince);
         dao.create(f2);
         
      } catch (SQLException var8) {
         System.out.println("Error trying to insert to the database!");
         var8.printStackTrace();
      }
   }

   public static long getFriendsSince(String pName, String fName) {
      try {
         Dao<Friendship, Integer> dao = Friends.database.getFriendshipDao();
         QueryBuilder<Friendship, Integer> qb = dao.queryBuilder();
         qb.where().eq("PlayerName", pName).and().eq("FriendName", fName);
         Friendship friendship = dao.queryForFirst(qb.prepare());
         
         if (friendship != null) {
            return friendship.getFriendsSince();
         }
      } catch (SQLException var7) {
         var7.printStackTrace();
      }
      return 0L;
   }

   public static boolean areFriends(String pName, String fName) {
      try {
         Dao<Friendship, Integer> dao = Friends.database.getFriendshipDao();
         QueryBuilder<Friendship, Integer> qb = dao.queryBuilder();
         qb.where().eq("PlayerName", pName).and().eq("FriendName", fName);
         return qb.countOf() > 0;
      } catch (SQLException var5) {
         var5.printStackTrace();
         return false;
      }
   }

   public static List<String> getFriends(String pname) {
      List<String> friendNames = new ArrayList<>();

      try {
         Dao<Friendship, Integer> dao = Friends.database.getFriendshipDao();
         QueryBuilder<Friendship, Integer> qb = dao.queryBuilder();
         qb.where().eq("PlayerName", pname);
         List<Friendship> friendships = dao.query(qb.prepare());

         for (Friendship f : friendships) {
            friendNames.add(f.getFriendName());
         }
      } catch (SQLException var5) {
         var5.printStackTrace();
      }

      return friendNames;
   }

   public static void deleteFriend(String pname, String fname) {
      try {
         Dao<Friendship, Integer> dao = Friends.database.getFriendshipDao();
         
         DeleteBuilder<Friendship, Integer> db1 = dao.deleteBuilder();
         db1.where().eq("PlayerName", pname).and().eq("FriendName", fname);
         db1.delete();

         DeleteBuilder<Friendship, Integer> db2 = dao.deleteBuilder();
         db2.where().eq("PlayerName", fname).and().eq("FriendName", pname);
         db2.delete();
      } catch (SQLException var6) {
         var6.printStackTrace();
      }
   }

   public static int friendCount(String pName) {
      try {
         Dao<Friendship, Integer> dao = Friends.database.getFriendshipDao();
         QueryBuilder<Friendship, Integer> qb = dao.queryBuilder();
         qb.where().eq("PlayerName", pName);
         return (int) qb.countOf();
      } catch (SQLException var2) {
         var2.printStackTrace();
         return 0;
      }
   }
}
