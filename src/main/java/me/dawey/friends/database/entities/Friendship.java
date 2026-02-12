package me.dawey.friends.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "friends")
public class Friendship {
    
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "player_id", canBeNull = false)
    private int playerId;

    @DatabaseField(columnName = "friend_id", canBeNull = false)
    private int friendId;

    @DatabaseField(columnName = "friends_since")
    private long friendsSince;

    public Friendship() {}

    public Friendship(int playerId, int friendId, long friendsSince) {
        this.playerId = playerId;
        this.friendId = friendId;
        this.friendsSince = friendsSince;
    }

    public int getId() {
        return id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public long getFriendsSince() {
        return friendsSince;
    }

    public void setFriendsSince(long friendsSince) {
        this.friendsSince = friendsSince;
    }
}
