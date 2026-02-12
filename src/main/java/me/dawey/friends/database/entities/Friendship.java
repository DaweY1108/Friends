package me.dawey.friends.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "friends")
public class Friendship {
    
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "PlayerName", canBeNull = false)
    private String playerName;

    @DatabaseField(columnName = "FriendName", canBeNull = false)
    private String friendName;

    @DatabaseField(columnName = "FriendsSince")
    private long friendsSince;

    public Friendship() {}

    public Friendship(String playerName, String friendName, long friendsSince) {
        this.playerName = playerName;
        this.friendName = friendName;
        this.friendsSince = friendsSince;
    }

    public int getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public long getFriendsSince() {
        return friendsSince;
    }

    public void setFriendsSince(long friendsSince) {
        this.friendsSince = friendsSince;
    }
}
